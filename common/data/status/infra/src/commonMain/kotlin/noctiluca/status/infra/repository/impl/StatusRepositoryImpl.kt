package noctiluca.status.infra.repository.impl

import noctiluca.api.mastodon.MastodonApiV1
import noctiluca.repository.TokenProvider
import noctiluca.status.infra.repository.StatusRepository
import noctiluca.status.infra.toEntity
import noctiluca.status.model.Status

internal class StatusRepositoryImpl(
    private val api: MastodonApiV1,
    private val tokenProvider: TokenProvider,
) : StatusRepository {
    override suspend fun favourite(status: Status): Status {
        val json =
            if (status.favourited)
                api.postStatusesFavourite(status.id.value)
            else
                api.postStatusesUnfavourite(status.id.value)

        return json.toEntity(tokenProvider.getCurrent()?.id)
    }

    override suspend fun boost(status: Status): Status {
        val json =
            if (status.reblogged)
                api.postStatusesReblog(status.id.value)
            else
                api.postStatusesUnreblog(status.id.value)

        return json.toEntity(tokenProvider.getCurrent()?.id)
    }

    override suspend fun bookmark(status: Status): Status {
        val json =
            if (status.bookmarked)
                api.postStatusesBookmark(status.id.value)
            else
                api.postStatusesUnbookmark(status.id.value)

        return json.toEntity(tokenProvider.getCurrent()?.id)
    }
}
