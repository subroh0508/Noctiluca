package noctiluca.data.timeline.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import noctiluca.data.status.toEntity
import noctiluca.data.timeline.TimelineRepository
import noctiluca.datastore.TokenDataStore
import noctiluca.model.StatusId
import noctiluca.model.timeline.StreamEvent
import noctiluca.network.mastodon.MastodonApiV1
import noctiluca.network.mastodon.MastodonStream
import noctiluca.network.mastodon.data.streaming.NetworkStreamEvent
import noctiluca.network.mastodon.data.streaming.Stream
import noctiluca.network.mastodon.data.streaming.StreamingType

internal class TimelineRepositoryImpl(
    private val api: MastodonApiV1,
    private val webSocket: MastodonStream,
    private val tokenDataStore: TokenDataStore,
) : TimelineRepository {
    override suspend fun fetchGlobal(
        onlyRemote: Boolean,
        onlyMedia: Boolean,
        maxId: StatusId?
    ) = api.getTimelinesPublic(
        remote = onlyRemote,
        onlyMedia = onlyMedia,
        maxId = maxId?.value,
    ).map { it.toEntity(tokenDataStore.getCurrent()?.id) }

    override suspend fun fetchLocal(
        onlyMedia: Boolean,
        maxId: StatusId?,
    ) = api.getTimelinesPublic(
        local = true,
        onlyMedia = onlyMedia,
        maxId = maxId?.value,
    ).map { it.toEntity(tokenDataStore.getCurrent()?.id) }

    override suspend fun fetchHome(
        maxId: StatusId?,
    ) = api.getTimelinesHome(
        maxId = maxId?.value,
    ).map { it.toEntity(tokenDataStore.getCurrent()?.id) }

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
                it.status.toEntity(tokenDataStore.getCurrent()?.id),
            )

            is NetworkStreamEvent.Payload.StatusEdited -> StreamEvent.StatusEdited(
                it.status.toEntity(tokenDataStore.getCurrent()?.id),
            )

            is NetworkStreamEvent.Payload.Deleted -> StreamEvent.Deleted(StatusId(it.id))
            else -> null
        }
    }
}
