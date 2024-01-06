package noctiluca.data.accountdetail.impl

import kotlinx.coroutines.flow.*
import noctiluca.data.accountdetail.AccountDetailRepository
import noctiluca.data.accountdetail.toAttributeEntity
import noctiluca.data.accountdetail.toValueObject
import noctiluca.data.status.toEntity
import noctiluca.datastore.AuthenticationTokenDataStore
import noctiluca.model.AccountId
import noctiluca.model.StatusId
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
    ): Flow<AccountAttributes> = accountAttributeStateFlow
        .onSubscription {
            val account = fetch(id)

            emit(fetch(id))
            emit(account.copy(relationships = fetchRelationships(id)))
        }
        .filterNotNull()

    override suspend fun fetchStatuses(
        id: AccountId,
        maxId: StatusId?,
        onlyMedia: Boolean,
        excludeReplies: Boolean,
    ) = v1.getAccountsStatuses(
        id.value,
        maxId?.value,
        onlyMedia,
        excludeReplies,
    ).map {
        it.toEntity(authenticationTokenDataStore.getCurrent()?.id)
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
}
