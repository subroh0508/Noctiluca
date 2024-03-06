package noctiluca.data.account.impl

import kotlinx.coroutines.flow.*
import noctiluca.data.account.AuthorizedAccountRepository
import noctiluca.data.account.toEntity
import noctiluca.datastore.AccountDataStore
import noctiluca.datastore.AuthorizationTokenDataStore
import noctiluca.model.*
import noctiluca.model.account.Account
import noctiluca.network.mastodon.MastodonApiV1

internal class AuthorizedAccountRepositoryImpl(
    private val v1: MastodonApiV1,
    private val authorizationTokenDataStore: AuthorizationTokenDataStore,
    private val accountDataStore: AccountDataStore,
) : AuthorizedAccountRepository {
    private val allStateFlow: MutableStateFlow<List<Account>> = MutableStateFlow(emptyList())

    override fun all() = allStateFlow.onStart {
        if (allStateFlow.value.isNotEmpty()) {
            return@onStart
        }

        refreshAll()
    }

    private suspend fun refreshAll() {
        val cache = accountDataStore.all()

        allStateFlow.value = cache
        allStateFlow.value = cache.map { runCatching { refresh(it.id) }.getOrNull() ?: it }
    }

    private suspend fun refresh(id: AccountId): Account {
        val (accessToken, domain) = getAccessToken(id) ?: throw AuthorizedAccountNotFoundException
        val account = v1.getVerifyAccountsCredentials(domain.value, accessToken).toEntity(domain)

        accountDataStore.add(account)

        return account
    }

    private suspend fun getAccessToken(id: AccountId): Pair<String, Domain>? {
        val accessToken = authorizationTokenDataStore.getAccessToken(id) ?: return null
        val domain = authorizationTokenDataStore.getDomain(id) ?: return null

        return accessToken to domain
    }
}
