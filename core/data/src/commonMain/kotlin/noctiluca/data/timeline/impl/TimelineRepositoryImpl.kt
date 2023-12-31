package noctiluca.data.timeline.impl

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import noctiluca.data.status.toEntity
import noctiluca.data.timeline.TimelineRepository
import noctiluca.datastore.AuthenticationTokenDataStore
import noctiluca.model.StatusId
import noctiluca.model.timeline.StreamEvent
import noctiluca.model.timeline.Timeline
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
    private val timelinesStateFlow = MutableStateFlow(emptyList<Timeline>())
    private var stream = listOf<Job>()

    private val initial = listOf(
        Timeline.Local(listOf(), onlyMedia = false),
        Timeline.Home(listOf()),
        Timeline.Global(listOf(), onlyRemote = false, onlyMedia = false),
    )

    override fun buildStream() = timelinesStateFlow

    override suspend fun start() {
        timelinesStateFlow.value = initial

        initial.foldIndexed(initial) { i, acc, timeline ->
            val next = appendStatuses(acc, i, timeline)

            timelinesStateFlow.value = next
            subscribe(i, timeline)

            next
        }
    }

    override suspend fun load(timeline: Timeline) {
        val index = timelinesStateFlow.value.indexOf(timeline)

        if (index == -1) {
            return
        }

        appendStatuses(timelinesStateFlow.value, index, timeline)
    }

    override suspend fun close() {
        timelinesStateFlow.value = listOf()
        stream.forEach { it.cancel() }
        stream = listOf()
    }

    private suspend fun appendStatuses(
        prev: List<Timeline>,
        index: Int,
        timeline: Timeline,
    ): List<Timeline> {
        val statuses = when (timeline) {
            is Timeline.Global -> fetchGlobal(timeline.onlyRemote, timeline.onlyMedia)
            is Timeline.Local -> fetchLocal(timeline.onlyMedia)
            is Timeline.Home -> fetchHome()
            is Timeline.HashTag -> listOf() // TODO
            is Timeline.List -> listOf() // TODO
        }

        return prev.set(index, timeline + statuses)
    }

    private suspend fun subscribe(index: Int, timeline: Timeline) {
        if (stream.getOrNull(index) != null) {
            return
        }

        val flow = when (timeline) {
            is Timeline.Global -> buildGlobalStream(timeline.onlyRemote, timeline.onlyMedia)
            is Timeline.Local -> buildLocalStream(timeline.onlyMedia)
            is Timeline.Home -> buildHomeStream()
            is Timeline.HashTag -> flow { } // TODO
            is Timeline.List -> flow { } // TODO
        }

        stream += collectEvent(index, flow)
    }

    private fun collectEvent(index: Int, flow: Flow<StreamEvent>) = flow.onEach { event ->
        val current = timelinesStateFlow.value[index]

        val next = when (event) {
            is StreamEvent.Updated -> current.insert(event.status)
            is StreamEvent.Deleted -> current - event.id
            is StreamEvent.StatusEdited -> current.replace(event.status)
        }

        timelinesStateFlow.value = timelinesStateFlow.value.set(index, next)
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

    private fun List<Timeline>.set(
        index: Int,
        timeline: Timeline,
    ): List<Timeline> = toMutableList().apply { set(index, timeline) }

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
