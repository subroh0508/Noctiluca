package noctiluca.data.account.impl

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
    override suspend fun getCurrent(): Pair<Account, Domain>? {
        val account = getCurrentAccount() ?: return null
        val domain = getCurrentDomain() ?: return null

        return account to domain
    }

    override suspend fun fetchAll() = authenticationTokenDataStore.getAll().mapNotNull {
        val cache = accountDataStore.get(it.id)
        if (cache != null) {
            return@mapNotNull cache
        }

        val account = runCatching {
            v1.getAccount(it.id.value).toEntity(it.domain)
        }.getOrNull() ?: return@mapNotNull null

        accountDataStore.add(account)
        account
    }

    override suspend fun fetchCurrent(): Pair<Account, Domain> {
        val domain = getCurrentDomain() ?: throw AuthorizedTokenNotFoundException

        val account = runCatching {
            v1.getVerifyAccountsCredentials(domain.value)
        }.getOrNull()?.toEntity(domain) ?: throw AuthorizedTokenNotFoundException

        accountDataStore.add(account)

        return account to domain
    }

    override suspend fun refresh(id: AccountId): Account {
        val (accessToken, domain) = getAccessToken(id) ?: throw AuthorizedAccountNotFoundException
        val account = v1.getVerifyAccountsCredentials(domain.value, accessToken).toEntity(domain)

        accountDataStore.add(account)

        return account
    }

    private suspend fun getCurrentAccount() = authenticationTokenDataStore.getCurrent()?.let {
        accountDataStore.get(it.id)
    }

    private suspend fun getCurrentDomain() = authenticationTokenDataStore.getCurrent()?.domain

    private suspend fun getAccessToken(id: AccountId): Pair<String, Domain>? {
        val accessToken = authenticationTokenDataStore.getAccessToken(id) ?: return null
        val domain = authenticationTokenDataStore.getDomain(id) ?: return null

        return accessToken to domain
    }
}
