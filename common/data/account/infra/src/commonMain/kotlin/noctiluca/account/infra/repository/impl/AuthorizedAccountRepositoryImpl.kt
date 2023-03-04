package noctiluca.account.infra.repository.impl

import noctiluca.account.infra.repository.AuthorizedAccountRepository
import noctiluca.account.infra.repository.local.LocalAccountCredentialCache
import noctiluca.account.model.Account
import noctiluca.api.mastodon.MastodonApiV1
import noctiluca.api.mastodon.json.account.AccountCredentialJson
import noctiluca.api.token.LocalTokenCache
import noctiluca.model.*
import noctiluca.repository.TokenProvider

internal class AuthorizedAccountRepositoryImpl(
    private val tokenProvider: TokenProvider,
    private val tokenCache: LocalTokenCache,
    private val accountCredentialCache: LocalAccountCredentialCache,
    private val v1: MastodonApiV1,
) : AuthorizedAccountRepository {
    override suspend fun fetchCurrent(): Account {
        val current = tokenProvider.getCurrent() ?: throw AuthorizedAccountNotFoundException

        return fetchAccountCredential(
            current.id,
            current.domain,
        )?.toEntity(current.domain) ?: throw AuthorizedAccountNotFoundException
    }

    override suspend fun refresh(id: AccountId, domain: Domain): Account {
        val accessToken = tokenCache.getAccessToken(id, domain)

        return fetchAccountCredential(
            id,
            domain,
            accessToken,
        )?.toEntity(domain) ?: throw AuthorizedAccountNotFoundException
    }

    private suspend fun fetchAccountCredential(
        id: AccountId,
        domain: Domain,
        accessToken: String? = null,
    ): AccountCredentialJson? {
        val fromApi = runCatching {
            v1.getVerifyAccountsCredentials(
                domain.value,
                accessToken,
            )
        }.getOrNull()

        return fromApi ?: runCatching {
            accountCredentialCache.get(id, domain)
        }.getOrNull()
    }

    private fun AccountCredentialJson.toEntity(
        domain: Domain,
    ) = Account(
        AccountId(id),
        username,
        displayName,
        domain,
        Uri(avatar),
    )
}