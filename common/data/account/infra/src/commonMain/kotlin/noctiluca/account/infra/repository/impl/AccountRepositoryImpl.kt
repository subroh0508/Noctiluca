package noctiluca.account.infra.repository.impl

import noctiluca.account.infra.repository.AccountRepository
import noctiluca.account.infra.repository.local.LocalAccountCredentialCache
import noctiluca.account.infra.toEntity
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
    override suspend fun fetchCurrentAuthorizedAccount(): Account {
        val current = tokenProvider.getCurrent() ?: throw AuthorizedTokenNotFoundException

        return v1.getVerifyAccountsCredentials(current.domain.value).toEntity(current.domain)
    }

    override suspend fun fetchAuthorizedAccount(user: AuthorizedUser): Account {
        val accessToken = tokenCache.getAccessToken(user.id, user.domain)
            ?: throw AuthorizedTokenNotFoundException

        return v1.getVerifyAccountsCredentials(
            user.domain.value,
            accessToken,
        ).toEntity(user.domain)
    }
}
