package noctiluca.account.infra.repository.impl

import noctiluca.account.infra.repository.AccountRepository
import noctiluca.account.model.Account
import noctiluca.api.mastodon.MastodonApiV1
import noctiluca.api.mastodon.json.account.AccountCredentialJson
import noctiluca.api.token.LocalTokenCache
import noctiluca.model.*
import noctiluca.repository.TokenProvider

internal class AccountRepositoryImpl(
    private val tokenProvider: TokenProvider,
    private val tokenCache: LocalTokenCache,
    private val v1: MastodonApiV1,
) : AccountRepository {
    override suspend fun fetchCurrentAccount(): Account {
        val current = tokenProvider.getCurrent() ?: throw AuthorizedTokenNotFoundException

        return v1.getVerifyAccountsCredentials(current.domain.value).toEntity(current.domain)
    }

    override suspend fun fetchAllAuthorizedAccounts(): List<Account> {
        val accounts = tokenProvider.getAuthorizedUsers().takeIf(List<AuthorizedUser>::isNotEmpty)
            ?: throw AuthorizedTokenNotFoundException

        return accounts.mapNotNull {
            val accessToken = tokenCache.getAccessToken(it.id, it.domain) ?: return@mapNotNull null

            v1.getVerifyAccountsCredentials(
                it.domain.value,
                accessToken,
            ).toEntity(it.domain)
        }
    }

    private fun AccountCredentialJson.toEntity(domain: Domain) = Account(
        AccountId(id),
        username,
        displayName,
        domain,
        Uri(avatar),
    )
}
