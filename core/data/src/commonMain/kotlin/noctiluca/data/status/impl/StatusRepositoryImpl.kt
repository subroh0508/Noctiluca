package noctiluca.data.status.impl

import noctiluca.data.status.StatusRepository
import noctiluca.data.status.toEntity
import noctiluca.datastore.AuthenticationTokenDataStore
import noctiluca.model.StatusId
import noctiluca.model.status.Status
import noctiluca.network.mastodon.MastodonApiV1

internal class StatusRepositoryImpl(
    private val api: MastodonApiV1,
    private val authenticationTokenDataStore: AuthenticationTokenDataStore,
) : StatusRepository {
    override suspend fun fetch(
        id: StatusId,
    ) = api.getStatus(id.value).toEntity(authenticationTokenDataStore.getCurrent()?.id)

    override suspend fun fetchContext(id: StatusId): List<Status> {
        val (ancestors, descendants) = api.getStatusesContext(id.value)

        return (ancestors + listOf(api.getStatus(id.value)) + descendants).map {
            it.toEntity(authenticationTokenDataStore.getCurrent()?.id)
        }
    }

    override suspend fun delete(
        id: StatusId,
    ) = api.deleteStatus(id.value).toEntity(authenticationTokenDataStore.getCurrent()?.id)

    override suspend fun favourite(status: Status): Status {
        val json =
            if (status.favourited) {
                api.postStatusesUnfavourite(status.id.value)
            } else {
                api.postStatusesFavourite(status.id.value)
            }

        return json.toEntity(authenticationTokenDataStore.getCurrent()?.id)
    }

    override suspend fun boost(status: Status): Status {
        val json =
            if (status.reblogged) {
                api.postStatusesUnreblog(status.id.value)
            } else {
                api.postStatusesReblog(status.id.value)
            }

        return json.toEntity(authenticationTokenDataStore.getCurrent()?.id)
    }

    override suspend fun bookmark(status: Status): Status {
        val json =
            if (status.bookmarked) {
                api.postStatusesUnbookmark(status.id.value)
            } else {
                api.postStatusesBookmark(status.id.value)
            }

        return json.toEntity(authenticationTokenDataStore.getCurrent()?.id)
    }
}
