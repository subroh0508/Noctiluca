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
import noctiluca.network.mastodon.MastodonApiV1
import noctiluca.network.mastodon.MastodonStream
import noctiluca.network.mastodon.data.streaming.NetworkStreamEvent
import noctiluca.network.mastodon.data.streaming.StreamingType

internal class TimelineRepositoryImpl(
    private val api: MastodonApiV1,
    private val webSocket: MastodonStream,
    private val authenticationTokenDataStore: AuthenticationTokenDataStore,
    private val streamCoroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO),
) : TimelineRepository {
    private val streamStateFlow = StreamStateFlow(StreamState())

    private val initial = mapOf(
        LocalTimelineId to Timeline.Local(listOf(), onlyMedia = false),
        HomeTimelineId to Timeline.Home(listOf()),
        GlobalTimelineId to Timeline.Global(listOf(), onlyRemote = false, onlyMedia = false),
    )

    override fun buildStream(): Flow<StreamState> = streamStateFlow

    override suspend fun start() {
        streamStateFlow.value = StreamState(timeline = initial)

        initial.forEach { (timelineId, timeline) ->
            streamStateFlow[timelineId] = timeline + fetchStatuses(timeline)
            subscribe(timelineId, timeline)
        }
    }

    override suspend fun load(timelineId: TimelineId) {
        val timeline = streamStateFlow.value.timeline(timelineId) ?: return

        streamStateFlow[timelineId] = timeline + fetchStatuses(timeline)
    }

    override suspend fun close() {
        streamStateFlow.clearTimeline()
        streamStateFlow.cancelAll()
    }

    private suspend fun subscribe(timelineId: TimelineId, timeline: Timeline) {
        if (streamStateFlow.hasActiveJob(timelineId)) {
            return
        }

        streamStateFlow[timelineId] = collectEvent(timelineId, buildStream(timeline))
    }

    private fun collectEvent(timelineId: TimelineId, flow: Flow<StreamEvent>) = flow.onEach { event ->
        val current = streamStateFlow.value.timeline(timelineId) ?: return@onEach

        val next = when (event) {
            is StreamEvent.Updated -> current.insert(event.status)
            is StreamEvent.Deleted -> current - event.id
            is StreamEvent.StatusEdited -> current.replace(event.status)
        }

        streamStateFlow[timelineId] = next
        streamStateFlow[timelineId] = event
    }.launchIn(streamCoroutineScope)

    private suspend fun fetchStatuses(
        timeline: Timeline,
    ): List<Status> {
        val data = when (timeline) {
            is Timeline.Global -> api.getTimelinesPublic(
                remote = timeline.onlyRemote,
                onlyMedia = timeline.onlyMedia,
                maxId = timeline.maxId?.value,
            )

            is Timeline.Local -> api.getTimelinesPublic(
                local = true,
                onlyMedia = timeline.onlyMedia,
                maxId = timeline.maxId?.value,
            )

            is Timeline.Home -> api.getTimelinesHome(
                maxId = timeline.maxId?.value,
            )

            is Timeline.HashTag -> listOf() // TODO
            is Timeline.List -> listOf() // TODO
        }

        return data.map { it.toEntity(authenticationTokenDataStore.getCurrent()?.id) }
    }

    private suspend fun buildStream(timeline: Timeline): Flow<StreamEvent> {
        val stream = timeline.toStream()

        return webSocket.streaming(
            stream.value,
            StreamingType.SUBSCRIBE.name.lowercase(),
        ).mapNotNull { it.toValueObject() }
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

    private class StreamStateFlow(
        private val state: StreamState,
    ) : MutableStateFlow<StreamState> by MutableStateFlow(state) {
        operator fun set(
            timelineId: TimelineId,
            timeline: Timeline,
        ) {
            value += (timelineId to timeline)
        }

        operator fun set(
            timelineId: TimelineId,
            job: Job,
        ) {
            value += (timelineId to job)
        }

        operator fun set(
            timelineId: TimelineId,
            event: StreamEvent,
        ) {
            value += (timelineId to event)
        }

        fun hasActiveJob(timelineId: TimelineId) = value.hasActiveJob(timelineId)

        fun clearTimeline() {
            value = value.copy(timeline = emptyMap())
        }

        fun cancelAll() {
            value.cancelAll()
            value = value.copy(stream = emptyMap(), latestEvent = emptyMap())
        }
    }
}
