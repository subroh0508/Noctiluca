package noctiluca.data.timeline.impl

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import noctiluca.data.status.toEntity
import noctiluca.data.timeline.TimelineRepository
import noctiluca.data.timeline.toStream
import noctiluca.datastore.AuthenticationTokenDataStore
import noctiluca.model.StatusId
import noctiluca.model.status.Status
import noctiluca.model.timeline.*
import noctiluca.network.mastodon.MastodonStream
import noctiluca.network.mastodon.data.streaming.NetworkStreamEvent
import noctiluca.network.mastodon.data.streaming.StreamingType

internal class TimelineRepositoryImpl(
    private val webSocket: MastodonStream,
    private val authenticationTokenDataStore: AuthenticationTokenDataStore,
    private val streamCoroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO + Job()),
) : TimelineRepository {
    private val timelineStreamStateFlow by lazy { TimelineStreamStateFlow(TimelineStreamState()) }

    override val stream get() = timelineStreamStateFlow

    override fun get(timelineId: TimelineId) = timelineStreamStateFlow.value.timeline(timelineId)

    override suspend fun fetchInitialTimeline() = mapOf(
        LocalTimelineId to Timeline.Local(listOf(), onlyMedia = false),
        HomeTimelineId to Timeline.Home(listOf()),
        GlobalTimelineId to Timeline.Global(listOf(), onlyRemote = false, onlyMedia = false),
    )

    override suspend fun subscribe(
        initial: Map<TimelineId, Timeline>,
        statuses: Map<TimelineId, List<Status>>,
    ) {
        timelineStreamStateFlow.value = TimelineStreamState(timeline = initial)

        initial.forEach { (timelineId, timeline) ->
            timelineStreamStateFlow[timelineId] = timeline + statuses[timelineId].orEmpty()

            if (!timelineStreamStateFlow.hasActiveJob(timelineId)) {
                timelineStreamStateFlow[timelineId] = buildStream(timelineId, timeline)
            }
        }
    }

    override suspend fun load(
        timelineId: TimelineId,
        timeline: Timeline,
    ) {
        timelineStreamStateFlow[timelineId] = timeline
    }

    override fun unsubscribe() {
        timelineStreamStateFlow.cancelAll()
        timelineStreamStateFlow.clear()
    }

    private suspend fun buildStream(
        timelineId: TimelineId,
        timeline: Timeline,
    ) = webSocket.streaming(
        timeline.toStream().value,
        StreamingType.SUBSCRIBE.name.lowercase(),
    ).mapNotNull {
        it.toValueObject()
    }.onEach { event ->
        collectEvent(timelineId, event)
    }.launchIn(streamCoroutineScope)

    private fun collectEvent(
        timelineId: TimelineId,
        event: StreamEvent,
    ) {
        val current = timelineStreamStateFlow.value.timeline(timelineId) ?: return

        val next = when (event) {
            is StreamEvent.Updated -> current.insert(event.status)
            is StreamEvent.Deleted -> current - event.id
            is StreamEvent.StatusEdited -> current.replace(event.status)
        }

        timelineStreamStateFlow[timelineId] = next
        timelineStreamStateFlow[timelineId] = event
    }

    private suspend fun NetworkStreamEvent.toValueObject() = payload?.let {
        when (it) {
            is NetworkStreamEvent.Payload.Updated -> StreamEvent.Updated(
                it.status.toEntity(authenticationTokenDataStore.getCurrent()?.id),
            )

            is NetworkStreamEvent.Payload.StatusEdited -> StreamEvent.StatusEdited(
                it.status.toEntity(authenticationTokenDataStore.getCurrent()?.id),
            )

            is NetworkStreamEvent.Payload.Deleted -> StreamEvent.Deleted(StatusId(it.id))
            else -> null
        }
    }
}
