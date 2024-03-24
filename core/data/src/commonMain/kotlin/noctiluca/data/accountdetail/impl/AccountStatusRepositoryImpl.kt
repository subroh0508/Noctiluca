package noctiluca.data.accountdetail.impl

import kotlinx.coroutines.flow.*
import noctiluca.data.accountdetail.AccountStatusRepository
import noctiluca.data.status.toEntity
import noctiluca.datastore.AuthorizationTokenDataStore
import noctiluca.model.AccountId
import noctiluca.model.accountdetail.StatusesQuery
import noctiluca.model.status.Status
import noctiluca.network.mastodon.MastodonApiV1

internal class AccountStatusRepositoryImpl(
    private val v1: MastodonApiV1,
    private val authorizationTokenDataStore: AuthorizationTokenDataStore,
) : AccountStatusRepository {
    private val statusesStateFlow by lazy { MutableStateFlow<Map<StatusesQuery, List<Status>>>(mapOf()) }

    override fun statuses(
        id: AccountId,
    ) = flow {
        emitAll(statusesStateFlow)
    }.onStart {
        statusesStateFlow.value = mapOf()

        StatusesQuery.entries.forEach { query ->
            statusesStateFlow.value = buildNextStateMap(query, fetchStatuses(id, query))
        }
    }

    override suspend fun loadStatuses(
        id: AccountId,
        query: StatusesQuery,
    ) {
        statusesStateFlow.value = buildNextStateMap(
            query,
            fetchStatuses(id, query),
        )
    }

    private suspend fun fetchStatuses(
        id: AccountId,
        query: StatusesQuery,
    ) = v1.getAccountsStatuses(
        id.value,
        getMaxId(query)?.value,
        query == StatusesQuery.ONLY_MEDIA,
        query != StatusesQuery.WITH_REPLIES,
    ).map { it.toEntity(authorizationTokenDataStore.getCurrent()?.id) }

    private fun buildNextStateMap(
        query: StatusesQuery,
        statuses: List<Status>,
    ) = statusesStateFlow.value + (query to (statusesStateFlow.value[query] ?: listOf()) + statuses)

    private fun getMaxId(query: StatusesQuery) = statusesStateFlow.value[query]?.lastOrNull()?.id
}
