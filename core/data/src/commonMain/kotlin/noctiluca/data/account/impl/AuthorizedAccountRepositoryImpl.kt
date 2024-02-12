package noctiluca.data.account.impl

import kotlinx.coroutines.flow.*
import noctiluca.data.account.AuthorizedAccountRepository
import noctiluca.data.account.toEntity
import noctiluca.datastore.AccountDataStore
import noctiluca.datastore.AuthorizationTokenDataStore
import noctiluca.datastore.getOthers
import noctiluca.model.*
import noctiluca.model.account.Account
import noctiluca.network.mastodon.MastodonApiV1

internal class AuthorizedAccountRepositoryImpl(
    private val v1: MastodonApiV1,
    private val authorizationTokenDataStore: AuthorizationTokenDataStore,
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
        val token = authorizationTokenDataStore.setCurrent(id)

        currentStateFlow.value = refresh(id) to token.domain
        allStateFlow.value = authorizationTokenDataStore.getOthers()
            .mapNotNull { accountDataStore.get(it.id) }
    }

    private suspend fun refreshCurrent() {
        val cache = getCurrent()

        if (cache == null) {
            currentStateFlow.value = fetchCurrent()
            return
        }

        currentStateFlow.value = cache
        runCatching { fetchCurrent() }
            .onSuccess { currentStateFlow.value = it }
            .onFailure { if (it is AuthorizedTokenNotFoundException) throw it }
    }

    private suspend fun refreshOthers() {
        val cache = authorizationTokenDataStore.getOthers()

        allStateFlow.value = cache.mapNotNull {
            accountDataStore.get(it.id)
        }
        allStateFlow.value = cache.mapNotNull {
            runCatching { refresh(it.id) }.getOrNull()
                ?: accountDataStore.get(it.id)
        }
    }

    private suspend fun getCurrent(): Pair<Account, Domain>? {
        val token = authorizationTokenDataStore.getCurrent() ?: return null

        val account = accountDataStore.get(token.id) ?: return null
        val domain = token.domain

        return account to domain
    }

    private suspend fun fetchCurrent(): Pair<Account, Domain> {
        val domain = authorizationTokenDataStore.getCurrent()?.domain ?: throw AuthorizedTokenNotFoundException
        val account = v1.getVerifyAccountsCredentials(domain.value).toEntity(domain)

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
        val accessToken = authorizationTokenDataStore.getAccessToken(id) ?: return null
        val domain = authorizationTokenDataStore.getDomain(id) ?: return null

        return accessToken to domain
    }
}
