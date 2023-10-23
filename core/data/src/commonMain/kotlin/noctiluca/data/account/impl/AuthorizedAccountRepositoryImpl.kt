package noctiluca.data.account.impl

import noctiluca.data.account.AuthorizedAccountRepository
import noctiluca.data.account.toEntity
import noctiluca.datastore.AccountDataStore
import noctiluca.datastore.TokenDataStore
import noctiluca.model.*
import noctiluca.model.account.Account
import noctiluca.network.mastodon.MastodonApiV1

internal class AuthorizedAccountRepositoryImpl(
    private val v1: MastodonApiV1,
    private val tokenDataStore: TokenDataStore,
    private val accountDataStore: AccountDataStore,
) : AuthorizedAccountRepository {
    override suspend fun getCurrent(): Pair<Account, Domain>? {
        val account = getCurrentAccount() ?: return null
        val domain = getCurrentDomain() ?: return null

        return account to domain
    }

    override suspend fun getAll() = tokenDataStore.getAll().mapNotNull {
        accountDataStore.get(it.id)
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

    private suspend fun getCurrentAccount() = tokenDataStore.getCurrent()?.let {
        accountDataStore.get(it.id)
    }

    private suspend fun getCurrentDomain() = tokenDataStore.getCurrent()?.domain

    private suspend fun getAccessToken(id: AccountId): Pair<String, Domain>? {
        val accessToken = tokenDataStore.getAccessToken(id) ?: return null
        val domain = tokenDataStore.getDomain(id) ?: return null

        return accessToken to domain
    }
}
