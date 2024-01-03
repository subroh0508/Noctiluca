package noctiluca.data.status.impl

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onStart
import noctiluca.data.status.StatusRepository
import noctiluca.data.status.toEntity
import noctiluca.datastore.AuthenticationTokenDataStore
import noctiluca.model.StatusId
import noctiluca.model.status.Status
import noctiluca.network.mastodon.MastodonApiV1
import noctiluca.network.mastodon.data.status.NetworkStatus

internal class StatusRepositoryImpl(
    private val api: MastodonApiV1,
    private val authenticationTokenDataStore: AuthenticationTokenDataStore,
) : StatusRepository {
    private val statusContextStateFlow: MutableStateFlow<List<Status>> by lazy { MutableStateFlow(listOf()) }

    override fun context(id: StatusId) = statusContextStateFlow.onStart {
        val status = fetch(id)
        statusContextStateFlow.value = listOf(status)
        statusContextStateFlow.value = fetchContext(status)
    }

    override suspend fun delete(
        id: StatusId,
    ) = api.deleteStatus(id.value).toEntity()

    override suspend fun favourite(status: Status): Status {
        val json =
            if (status.favourited) {
                api.postStatusesUnfavourite(status.id.value)
            } else {
                api.postStatusesFavourite(status.id.value)
            }

        return json.toEntity()
    }

    override suspend fun boost(status: Status): Status {
        val json =
            if (status.reblogged) {
                api.postStatusesUnreblog(status.id.value)
            } else {
                api.postStatusesReblog(status.id.value)
            }

        return json.toEntity()
    }

    override suspend fun bookmark(status: Status): Status {
        val json =
            if (status.bookmarked) {
                api.postStatusesUnbookmark(status.id.value)
            } else {
                api.postStatusesBookmark(status.id.value)
            }

        return json.toEntity()
    }

    private suspend fun fetch(id: StatusId) = api.getStatus(id.value).toEntity()

    private suspend fun fetchContext(status: Status): List<Status> {
        val (ancestors, descendants) = api.getStatusesContext(status.id.value).let { (anc, des) ->
            anc.map { it.toEntity() } to des.map { it.toEntity() }
        }

        return ancestors + listOf(status) + descendants
    }

    private suspend fun NetworkStatus.toEntity() = toEntity(authenticationTokenDataStore.getCurrent()?.id)
}
