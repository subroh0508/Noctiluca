package noctiluca.data.accountdetail.impl

import kotlinx.coroutines.flow.*
import noctiluca.data.accountdetail.AccountRelationshipsRepository
import noctiluca.data.accountdetail.toValueObject
import noctiluca.datastore.AuthorizationTokenDataStore
import noctiluca.model.AccountId
import noctiluca.model.accountdetail.Relationships
import noctiluca.network.mastodon.MastodonApiV1
import noctiluca.network.mastodon.data.account.NetworkRelationship

internal class AccountRelationshipsRepositoryImpl(
    private val v1: MastodonApiV1,
    private val authorizationTokenDataStore: AuthorizationTokenDataStore,
) : AccountRelationshipsRepository {
    private val current get() = accountRelationshipsStateFlow.value
    private val accountRelationshipsStateFlow by lazy { MutableStateFlow(Relationships.NONE) }

    override fun relationships(
        id: AccountId,
    ): Flow<Relationships> = flow {
        emitAll(accountRelationshipsStateFlow)
    }.onStart {
        accountRelationshipsStateFlow.value = fetchRelationships(id)
    }

    override suspend fun follow(id: AccountId) {
        if (current.me || current.blocking || current.blockedBy) {
            return
        }

        val json =
            if (current.following) {
                v1.postAccountsUnfollow(id.value)
            } else {
                v1.postAccountsFollow(id.value)
            }

        accountRelationshipsStateFlow.value = json.toValueObject()
    }

    override suspend fun block(id: AccountId) {
        if (current.me) {
            return
        }

        val json =
            if (current.blocking) {
                v1.postAccountsUnblock(id.value)
            } else {
                v1.postAccountsBlock(id.value)
            }

        accountRelationshipsStateFlow.value = json.toValueObject()
    }

    override suspend fun mute(id: AccountId) {
        if (current.me) {
            return
        }

        val json =
            if (current.muting) {
                v1.postAccountsUnmute(id.value)
            } else {
                v1.postAccountsMute(id.value)
            }

        accountRelationshipsStateFlow.value = json.toValueObject()
    }

    override suspend fun toggleReblog(id: AccountId) {
        if (current.me) {
            return
        }

        accountRelationshipsStateFlow.value = v1.postAccountsFollow(
            id.value,
            reblogs = !current.showReblogs,
            notify = current.notifying,
        ).toValueObject()
    }

    override suspend fun toggleNotify(id: AccountId) {
        if (current.me) {
            return
        }

        accountRelationshipsStateFlow.value = v1.postAccountsFollow(
            id.value,
            reblogs = current.showReblogs,
            notify = !current.notifying,
        ).toValueObject()
    }

    private suspend fun fetchRelationships(
        id: AccountId,
    ): Relationships {
        if (id == authorizationTokenDataStore.getCurrent()?.id) {
            return Relationships.ME
        }

        val json = v1.getAccountsRelationships(listOf(id.value))
            .find { it.id == id.value }
            ?: return Relationships.NONE

        return json.toValueObject()
    }

    private suspend fun NetworkRelationship.toValueObject() = toValueObject(
        authorizationTokenDataStore.getCurrent(),
    )
}
