package noctiluca.account.infra.repository.local.impl

import noctiluca.account.infra.repository.local.LocalAuthorizedAccountRepository
import noctiluca.datastore.AccountDataStore
import noctiluca.datastore.TokenDataStore
import noctiluca.model.AccountId
import noctiluca.model.Domain
import noctiluca.model.account.Account
import noctiluca.repository.TokenProvider

internal class LocalAuthorizedAccountRepositoryImpl(
    private val tokenProvider: TokenProvider,
    private val tokenDataStore: TokenDataStore,
    private val accountDataStore: AccountDataStore,
) : LocalAuthorizedAccountRepository {
    override suspend fun getCurrentAccount() = tokenProvider.getCurrent()?.let { user ->
        accountDataStore.get(user.id)
    }

    override suspend fun getCurrentDomain() = tokenProvider.getCurrent()?.domain

    override suspend fun getAll() = tokenDataStore.getAll().mapNotNull {
        accountDataStore.get(it.id)
    }

    override suspend fun getAccessToken(id: AccountId): Pair<String, Domain>? {
        val accessToken = tokenDataStore.getAccessToken(id) ?: return null
        val domain = tokenDataStore.getDomain(id) ?: return null

        return accessToken to domain
    }

    override suspend fun save(account: Account) {
        accountDataStore.add(account)
    }
}
