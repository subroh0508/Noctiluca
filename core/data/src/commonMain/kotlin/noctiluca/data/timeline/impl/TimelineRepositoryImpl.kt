package noctiluca.data.timeline.impl

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import noctiluca.data.status.toEntity
import noctiluca.data.timeline.TimelineRepository
import noctiluca.datastore.AuthenticationTokenDataStore
import noctiluca.model.StatusId
import noctiluca.model.timeline.*
import noctiluca.network.mastodon.MastodonApiV1
import noctiluca.network.mastodon.MastodonStream
import noctiluca.network.mastodon.data.streaming.NetworkStreamEvent
import noctiluca.network.mastodon.data.streaming.Stream
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

        appendStatuses(timeline)
    }

    override suspend fun close() {
        streamStateFlow.clear()
        streamStateFlow.cancelAll()
    }

    private suspend fun appendStatuses(
        timeline: Timeline,
    ): Timeline {
        val statuses = when (timeline) {
            is Timeline.Global -> fetchGlobal(timeline.onlyRemote, timeline.onlyMedia)
            is Timeline.Local -> fetchLocal(timeline.onlyMedia)
            is Timeline.Home -> fetchHome()
            is Timeline.HashTag -> listOf() // TODO
            is Timeline.List -> listOf() // TODO
        }

        return timeline + statuses
    }

    private suspend fun subscribe(timelineId: TimelineId, timeline: Timeline) {
        val flow = when (timeline) {
            is Timeline.Global -> buildGlobalStream(timeline.onlyRemote, timeline.onlyMedia)
            is Timeline.Local -> buildLocalStream(timeline.onlyMedia)
            is Timeline.Home -> buildHomeStream()
            is Timeline.HashTag -> flow { } // TODO
            is Timeline.List -> flow { } // TODO
        }

        streamStateFlow[timelineId] = collectEvent(timelineId, flow)
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

    override suspend fun fetchGlobal(
        onlyRemote: Boolean,
        onlyMedia: Boolean,
        maxId: StatusId?
    ) = api.getTimelinesPublic(
        remote = onlyRemote,
        onlyMedia = onlyMedia,
        maxId = maxId?.value,
    ).map { it.toEntity(authenticationTokenDataStore.getCurrent()?.id) }

    override suspend fun fetchLocal(
        onlyMedia: Boolean,
        maxId: StatusId?,
    ) = api.getTimelinesPublic(
        local = true,
        onlyMedia = onlyMedia,
        maxId = maxId?.value,
    ).map { it.toEntity(authenticationTokenDataStore.getCurrent()?.id) }

    override suspend fun fetchHome(
        maxId: StatusId?,
    ) = api.getTimelinesHome(
        maxId = maxId?.value,
    ).map { it.toEntity(authenticationTokenDataStore.getCurrent()?.id) }

    override suspend fun buildGlobalStream(
        onlyRemote: Boolean,
        onlyMedia: Boolean,
    ): Flow<StreamEvent> {
        val stream = when {
            onlyRemote && onlyMedia -> Stream.PUBLIC_REMOTE_MEDIA
            onlyRemote && !onlyMedia -> Stream.PUBLIC_REMOTE
            !onlyRemote && onlyMedia -> Stream.PUBLIC_MEDIA
            else -> Stream.PUBLIC
        }

        return webSocket.streaming(
            stream.value,
            StreamingType.SUBSCRIBE.name.lowercase(),
        ).mapNotNull { it.toValueObject() }
    }

    override suspend fun buildLocalStream(
        onlyMedia: Boolean
    ): Flow<StreamEvent> {
        val stream = if (onlyMedia) Stream.PUBLIC_LOCAL_MEDIA else Stream.PUBLIC_LOCAL

        return webSocket.streaming(
            stream.value,
            StreamingType.SUBSCRIBE.name.lowercase(),
        ).mapNotNull { it.toValueObject() }
    }

    override suspend fun buildHomeStream() = webSocket.streaming(
        Stream.USER.value,
        StreamingType.SUBSCRIBE.name.lowercase(),
    ).mapNotNull { it.toValueObject() }

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
            @JvmName("_plusAssign1")
            operator fun plusAssign(
                value: Map<TimelineId, Timeline>,
            ) {
                this.value = state.copy(timeline = value)
            }

            @JvmName("_plusAssign2")
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
