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
    private val streamStateFlow = StreamState.Flow(StreamState())

    private val initial = mapOf(
        LocalTimelineId to Timeline.Local(listOf(), onlyMedia = false),
        HomeTimelineId to Timeline.Home(listOf()),
        GlobalTimelineId to Timeline.Global(listOf(), onlyRemote = false, onlyMedia = false),
    )

    override fun buildStream() = streamStateFlow.map { it.timeline }

    override suspend fun start() {
        streamStateFlow += initial

        initial.forEach { (timelineId, timeline) ->
            streamStateFlow[timelineId] = appendStatuses(timeline)
            subscribe(timelineId, timeline)
        }
    }

    override suspend fun load(timelineId: TimelineId) {
        val timeline = streamStateFlow.value.timeline[timelineId] ?: return

        streamStateFlow[timelineId] = appendStatuses(timeline)
    }

    override suspend fun close() {
        streamStateFlow.clear()
        streamStateFlow.cancelAll()
    }

    private suspend fun appendStatuses(timeline: Timeline) = timeline + fetchStatuses(timeline)

    private suspend fun subscribe(timelineId: TimelineId, timeline: Timeline) {
        streamStateFlow[timelineId] = collectEvent(timelineId, buildStream(timeline))
    }

    private fun collectEvent(timelineId: TimelineId, flow: Flow<StreamEvent>) = flow.onEach { event ->
        val current = streamStateFlow.value.timeline[timelineId] ?: return@onEach

        val next = when (event) {
            is StreamEvent.Updated -> current.insert(event.status)
            is StreamEvent.Deleted -> current - event.id
            is StreamEvent.StatusEdited -> current.replace(event.status)
        }

        streamStateFlow[timelineId] = next
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

    private data class StreamState(
        val timeline: Map<TimelineId, Timeline> = emptyMap(),
        val stream: Map<TimelineId, Job> = emptyMap(),
    ) {
        class Flow(
            private val state: StreamState,
        ) : MutableStateFlow<StreamState> by MutableStateFlow(state) {
            @JvmName("_plusAssignTimeline")
            operator fun plusAssign(
                value: Map<TimelineId, Timeline>,
            ) {
                this.value = state.copy(timeline = value)
            }

            @JvmName("_plusAssignJob")
            operator fun plusAssign(
                value: Map<TimelineId, Job>,
            ) {
                this.value = state.copy(stream = value)
            }

            operator fun set(
                timelineId: TimelineId,
                timeline: Timeline,
            ) {
                value = value.copy(timeline = value.timeline + (timelineId to timeline))
            }

            operator fun set(
                timelineId: TimelineId,
                job: Job,
            ) {
                value = value.copy(stream = value.stream + (timelineId to job))
            }

            fun clear() {
                value = value.copy(timeline = emptyMap())
            }

            fun cancelAll() {
                value.stream.forEach { (_, job) -> job.cancel() }
                value = value.copy(stream = emptyMap())
            }
        }
    }
}
