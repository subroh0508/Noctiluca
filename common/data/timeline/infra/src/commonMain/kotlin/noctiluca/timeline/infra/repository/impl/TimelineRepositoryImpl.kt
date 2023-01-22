package noctiluca.timeline.infra.repository.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import noctiluca.api.mastodon.MastodonApiV1
import noctiluca.api.mastodon.MastodonStream
import noctiluca.api.mastodon.json.status.StatusJson
import noctiluca.api.mastodon.json.streaming.Event
import noctiluca.api.mastodon.json.streaming.Stream
import noctiluca.api.mastodon.json.streaming.StreamEventJson
import noctiluca.api.mastodon.json.streaming.StreamingType
import noctiluca.model.StatusId
import noctiluca.repository.TokenProvider
import noctiluca.status.infra.toEntity
import noctiluca.timeline.infra.repository.TimelineRepository
import noctiluca.timeline.model.StreamEvent

internal class TimelineRepositoryImpl(
    private val api: MastodonApiV1,
    private val webSocket: MastodonStream,
    private val tokenProvider: TokenProvider,
) : TimelineRepository {
    override suspend fun fetchGlobal(
        onlyRemote: Boolean,
        onlyMedia: Boolean,
        maxId: StatusId?
    ) = api.getTimelinesPublic(
        remote = onlyRemote,
        onlyMedia = onlyMedia,
        maxId = maxId?.value,
    ).map { it.toEntity(tokenProvider.getCurrent()?.id) }

    override suspend fun fetchLocal(
        onlyMedia: Boolean,
        maxId: StatusId?,
    ) = api.getTimelinesPublic(
        local = true,
        onlyMedia = onlyMedia,
        maxId = maxId?.value,
    ).map { it.toEntity(tokenProvider.getCurrent()?.id) }

    override suspend fun fetchHome(
        maxId: StatusId?,
    ) = api.getTimelinesHome(
        maxId = maxId?.value,
    ).map { it.toEntity(tokenProvider.getCurrent()?.id) }

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

    private suspend fun StreamEventJson.toValueObject() = when (Event.findEvent(event)) {
        Event.UPDATE -> payload?.let {
            StreamEvent.Updated(
                json.decodeFromString(StatusJson.serializer(), it)
                    .toEntity(tokenProvider.getCurrent()?.id),
            )
        }
        Event.DELETE -> payload?.let {
            StreamEvent.Deleted(StatusId(it))
        }
        else -> null
    }

    private val json get() = webSocket.json
}