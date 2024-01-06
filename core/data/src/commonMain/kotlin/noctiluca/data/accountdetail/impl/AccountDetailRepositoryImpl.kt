package noctiluca.data.accountdetail.impl

import kotlinx.coroutines.flow.*
import noctiluca.data.accountdetail.AccountDetailRepository
import noctiluca.data.accountdetail.toAttributeEntity
import noctiluca.data.accountdetail.toValueObject
import noctiluca.data.status.toEntity
import noctiluca.datastore.AuthenticationTokenDataStore
import noctiluca.model.AccountId
import noctiluca.model.accountdetail.AccountAttributes
import noctiluca.model.accountdetail.Relationships
import noctiluca.model.accountdetail.StatusesQuery
import noctiluca.model.status.Status
import noctiluca.network.mastodon.MastodonApiV1

internal class AccountDetailRepositoryImpl(
    private val v1: MastodonApiV1,
    private val authenticationTokenDataStore: AuthenticationTokenDataStore,
) : AccountDetailRepository {
    private val accountAttributeStateFlow by lazy { MutableStateFlow<AccountAttributes?>(null) }
    private val statusesStateFlow by lazy { MutableStateFlow<Map<StatusesQuery, List<Status>>>(mapOf()) }

    override fun attributes(
        id: AccountId,
    ): Flow<AccountAttributes> = accountAttributeStateFlow
        .onSubscription {
            val account = fetch(id)

            emit(fetch(id))
            emit(account.copy(relationships = fetchRelationships(id)))
        }
        .filterNotNull()

    override fun statuses(
        id: AccountId,
    ): Flow<Map<StatusesQuery, List<Status>>> = flow {
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
            fetchStatuses(id, query)
        )
    }

    private suspend fun fetch(id: AccountId) = v1.getAccount(id.value).toAttributeEntity()

    private suspend fun fetchRelationships(
        id: AccountId,
    ): Relationships {
        if (id == authenticationTokenDataStore.getCurrent()?.id) {
            return Relationships.ME
        }

        val json = v1.getAccountsRelationships(listOf(id.value))
            .find { it.id == id.value }
            ?: return Relationships.NONE

        return json.toValueObject(authenticationTokenDataStore.getCurrent())
    }

    private suspend fun fetchStatuses(
        id: AccountId,
        query: StatusesQuery,
    ) = v1.getAccountsStatuses(
        id.value,
        getMaxId(query)?.value,
        query == StatusesQuery.ONLY_MEDIA,
        query != StatusesQuery.WITH_REPLIES,
    ).map { it.toEntity(authenticationTokenDataStore.getCurrent()?.id) }

    private fun buildNextStateMap(
        query: StatusesQuery,
        statuses: List<Status>,
    ) = statusesStateFlow.value + (query to statusesStateFlow.value.getOrDefault(query, listOf()) + statuses)

    private fun getMaxId(query: StatusesQuery) = statusesStateFlow.value[query]?.lastOrNull()?.id
}
