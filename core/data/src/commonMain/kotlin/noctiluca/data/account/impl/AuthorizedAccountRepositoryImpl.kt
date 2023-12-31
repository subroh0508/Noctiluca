package noctiluca.data.account.impl

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onStart
import noctiluca.data.account.AuthorizedAccountRepository
import noctiluca.data.account.toEntity
import noctiluca.datastore.AccountDataStore
import noctiluca.datastore.AuthenticationTokenDataStore
import noctiluca.model.*
import noctiluca.model.account.Account
import noctiluca.network.mastodon.MastodonApiV1

internal class AuthorizedAccountRepositoryImpl(
    private val v1: MastodonApiV1,
    private val authenticationTokenDataStore: AuthenticationTokenDataStore,
    private val accountDataStore: AccountDataStore,
) : AuthorizedAccountRepository {
    private val currentStateFlow: MutableStateFlow<Pair<Account, Domain>?> = MutableStateFlow(null)
    private val allStateFlow: MutableStateFlow<List<Account>> = MutableStateFlow(emptyList())

    override fun current() = currentStateFlow.onStart {
        if (currentStateFlow.value != null) {
            return@onStart
        }

        refreshCurrent()
    }

    override fun others() = allStateFlow.onStart {
        if (allStateFlow.value.isNotEmpty()) {
            return@onStart
        }

        refreshOthers()
    }

    override suspend fun switch(id: AccountId) {
        val token = authenticationTokenDataStore.setCurrent(id)

        currentStateFlow.value = refresh(id) to token.domain
        allStateFlow.value = authenticationTokenDataStore.getAll().drop(1).mapNotNull { accountDataStore.get(it.id) }
    }

    private suspend fun refreshCurrent() {
        val cache = getCurrent()

        if (cache == null) {
            currentStateFlow.value = fetchCurrent()
            return
        }

        val (account, domain) = cache
        currentStateFlow.value = cache
        currentStateFlow.value = refresh(account.id) to domain
    }

    private suspend fun refreshOthers() {
        val cache = authenticationTokenDataStore.getAll().drop(1)

        allStateFlow.value = cache.mapNotNull { accountDataStore.get(it.id) }
        allStateFlow.value = cache.mapNotNull { runCatching { refresh(it.id) }.getOrNull() }
    }

    private suspend fun getCurrent(): Pair<Account, Domain>? {
        val token = authenticationTokenDataStore.getCurrent() ?: return null

        val account = accountDataStore.get(token.id) ?: return null
        val domain = token.domain

        return account to domain
    }

    private suspend fun fetchCurrent(): Pair<Account, Domain> {
        val domain = authenticationTokenDataStore.getCurrent()?.domain ?: throw AuthorizedTokenNotFoundException

        val account = runCatching {
            v1.getVerifyAccountsCredentials(domain.value)
        }.getOrNull()?.toEntity(domain) ?: throw AuthorizedTokenNotFoundException

        accountDataStore.add(account)

        return account to domain
    }

    private suspend fun refresh(id: AccountId): Account {
        val (accessToken, domain) = getAccessToken(id) ?: throw AuthorizedAccountNotFoundException
        val account = v1.getVerifyAccountsCredentials(domain.value, accessToken).toEntity(domain)

        accountDataStore.add(account)

        return account
    }

    private suspend fun getAccessToken(id: AccountId): Pair<String, Domain>? {
        val accessToken = authenticationTokenDataStore.getAccessToken(id) ?: return null
        val domain = authenticationTokenDataStore.getDomain(id) ?: return null

        return accessToken to domain
    }
}
