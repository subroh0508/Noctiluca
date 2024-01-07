package noctiluca.data.accountdetail.impl

import kotlinx.coroutines.flow.*
import noctiluca.data.accountdetail.AccountDetailRepository
import noctiluca.data.accountdetail.toAttributeEntity
import noctiluca.data.accountdetail.toValueObject
import noctiluca.datastore.AuthenticationTokenDataStore
import noctiluca.model.AccountId
import noctiluca.model.accountdetail.AccountAttributes
import noctiluca.model.accountdetail.Relationships
import noctiluca.network.mastodon.MastodonApiV1

internal class AccountDetailRepositoryImpl(
    private val v1: MastodonApiV1,
    private val authenticationTokenDataStore: AuthenticationTokenDataStore,
) : AccountDetailRepository {
    private val accountAttributeStateFlow by lazy { MutableStateFlow<AccountAttributes?>(null) }

    override fun attributes(
        id: AccountId,
    ): Flow<AccountAttributes> = flow {
        emitAll(accountAttributeStateFlow)
    }.onStart {
        val account = fetch(id)

        accountAttributeStateFlow.value = account
        accountAttributeStateFlow.value = account.copy(relationships = fetchRelationships(id))
    }
        .filterNotNull()

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
}
