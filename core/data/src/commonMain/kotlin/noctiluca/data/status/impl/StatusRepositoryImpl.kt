package noctiluca.data.status.impl

import noctiluca.data.status.StatusRepository
import noctiluca.data.status.toEntity
import noctiluca.datastore.TokenDataStore
import noctiluca.model.status.Status
import noctiluca.network.mastodon.MastodonApiV1

internal class StatusRepositoryImpl(
    private val api: noctiluca.network.mastodon.MastodonApiV1,
    private val tokenDataStore: TokenDataStore,
) : StatusRepository {
    override suspend fun favourite(status: Status): Status {
        val json =
            if (status.favourited) {
                api.postStatusesUnfavourite(status.id.value)
            } else {
                api.postStatusesFavourite(status.id.value)
            }

        return json.toEntity(tokenDataStore.getCurrent()?.id)
    }

    override suspend fun boost(status: Status): Status {
        val json =
            if (status.reblogged) {
                api.postStatusesUnreblog(status.id.value)
            } else {
                api.postStatusesReblog(status.id.value)
            }

        return json.toEntity(tokenDataStore.getCurrent()?.id)
    }

    override suspend fun bookmark(status: Status): Status {
        val json =
            if (status.bookmarked) {
                api.postStatusesUnbookmark(status.id.value)
            } else {
                api.postStatusesBookmark(status.id.value)
            }

        return json.toEntity(tokenDataStore.getCurrent()?.id)
    }
}
