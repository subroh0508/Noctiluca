package noctiluca.data.timeline.impl

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import noctiluca.data.status.toEntity
import noctiluca.data.timeline.TimelineRepository
import noctiluca.data.timeline.toStream
import noctiluca.datastore.AuthorizationTokenDataStore
import noctiluca.model.StatusId
import noctiluca.model.status.Status
import noctiluca.model.timeline.*
import noctiluca.network.mastodon.MastodonStream
import noctiluca.network.mastodon.data.streaming.NetworkStreamEvent
import noctiluca.network.mastodon.data.streaming.StreamingType

internal class TimelineRepositoryImpl(
    private val webSocket: MastodonStream,
    private val authorizationTokenDataStore: AuthorizationTokenDataStore,
    private val streamCoroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO + Job()),
) : TimelineRepository {
    private val timelineStreamStateFlow by lazy { TimelineStreamStateFlow(TimelineStreamState()) }

    override val stream get() = timelineStreamStateFlow

    override fun get(timelineId: TimelineId) = timelineStreamStateFlow.timeline(timelineId)

    override suspend fun fetchInitialTimeline() = mapOf(
        LocalTimelineId to Timeline.Local(listOf(), onlyMedia = false, isActive = false),
        HomeTimelineId to Timeline.Home(listOf(), isActive = false),
        GlobalTimelineId to Timeline.Global(listOf(), onlyRemote = false, onlyMedia = false, isActive = false),
    )

    override suspend fun subscribe(
        initial: Map<TimelineId, Timeline>,
        statuses: Map<TimelineId, List<Status>>,
    ) {
        initial.forEach { (timelineId, timeline) ->
            timelineStreamStateFlow[timelineId] = timeline + statuses[timelineId].orEmpty()

            if (!timelineStreamStateFlow.hasActiveJob(timelineId)) {
                timelineStreamStateFlow[timelineId] = buildStream(timelineId, timeline)
            }
        }
    }

    override fun load(
        timelineId: TimelineId,
        timeline: Timeline,
    ) {
        timelineStreamStateFlow[timelineId] = timeline
    }

    override fun update(status: Status) = timelineStreamStateFlow.update(status)

    override fun favourite(status: Status) = timelineStreamStateFlow.favourite(status)

    override fun boost(status: Status) = timelineStreamStateFlow.boost(status)

    override fun bookmark(status: Status) = timelineStreamStateFlow.bookmark(status)

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
        val current = timelineStreamStateFlow.timeline(timelineId) ?: return

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
                it.status.toEntity(authorizationTokenDataStore.getCurrent()?.id),
            )

            is NetworkStreamEvent.Payload.StatusEdited -> StreamEvent.StatusEdited(
                it.status.toEntity(authorizationTokenDataStore.getCurrent()?.id),
            )

            is NetworkStreamEvent.Payload.Deleted -> StreamEvent.Deleted(StatusId(it.id))
            else -> null
        }
    }
}
