package noctiluca.account.infra.repository.local.impl

import noctiluca.account.infra.repository.local.LocalAccountCredentialCache
import noctiluca.account.infra.repository.local.LocalAuthorizedAccountRepository
import noctiluca.account.infra.toEntity
import noctiluca.api.mastodon.json.account.AccountCredentialJson
import noctiluca.api.token.LocalTokenCache
import noctiluca.model.AccountId
import noctiluca.model.Domain
import noctiluca.repository.TokenProvider

internal class LocalAuthorizedAccountRepositoryImpl(
    private val tokenProvider: TokenProvider,
    private val tokenCache: LocalTokenCache,
    private val accountCredentialCache: LocalAccountCredentialCache,
) : LocalAuthorizedAccountRepository {
    override suspend fun getCurrentAccount() = tokenProvider.getCurrent()?.let { user ->
        accountCredentialCache.get(user.id)
            ?.toEntity(user.domain)
    }

    override suspend fun getCurrentDomain() = tokenProvider.getCurrent()?.domain

    override suspend fun getAll() = tokenCache.getAll().mapNotNull {
        accountCredentialCache.get(it.id)?.toEntity(it.domain)
    }

    override suspend fun getAccessToken(id: AccountId): Pair<String, Domain>? {
        val accessToken = tokenCache.getAccessToken(id) ?: return null
        val domain = tokenCache.getDomain(id) ?: return null

        return accessToken to domain
    }

    override suspend fun save(json: AccountCredentialJson) {
        accountCredentialCache.add(json)
    }
}
