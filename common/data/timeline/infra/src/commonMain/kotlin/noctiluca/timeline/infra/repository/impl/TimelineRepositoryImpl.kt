package noctiluca.timeline.infra.repository.impl

import noctiluca.api.mastodon.MastodonApiV1
import noctiluca.model.StatusId
import noctiluca.repository.TokenProvider
import noctiluca.status.infra.toEntity
import noctiluca.timeline.infra.repository.TimelineRepository

internal class TimelineRepositoryImpl(
    private val api: MastodonApiV1,
    private val tokenProvider: TokenProvider,
) : TimelineRepository {
    override suspend fun fetchLocal(
        onlyMedia: Boolean,
        maxId: StatusId?,
    ) = api.getTimelinesPublic(
        local = true,
        onlyMedia = onlyMedia,
        maxId = maxId?.value,
    ).map { it.toEntity(tokenProvider.getCurrent()?.id) }

    override suspend fun fetchGlobal(
        remote: Boolean,
        onlyMedia: Boolean,
        maxId: StatusId?
    ) = api.getTimelinesPublic(
        remote = remote,
        onlyMedia = onlyMedia,
        maxId = maxId?.value,
    ).map { it.toEntity(tokenProvider.getCurrent()?.id) }
}