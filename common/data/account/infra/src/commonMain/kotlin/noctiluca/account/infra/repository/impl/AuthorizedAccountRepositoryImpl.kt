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
    override suspend fun getCurrent() = tokenProvider.getCurrent()?.let { user ->
        accountCredentialCache.get(user.id)
            ?.toEntity(user.domain)
            ?.let { it to user.domain }
    }

    override suspend fun getAll() = tokenCache.getAll().mapNotNull {
        println(it)
        accountCredentialCache.get(it.id)?.toEntity(it.domain)
    }

    override suspend fun fetchCurrent(): Pair<Account, Domain> {
        val current = tokenProvider.getCurrent() ?: throw AuthorizedAccountNotFoundException

        val json = runCatching {
            v1.getVerifyAccountsCredentials(current.domain.value)
        }.getOrNull() ?: throw AuthorizedAccountNotFoundException

        accountCredentialCache.add(json)

        return json.toEntity(current.domain) to current.domain
    }

    override suspend fun refresh(id: AccountId): Account {
        val accessToken = tokenCache.getAccessToken(id) ?: throw AuthorizedAccountNotFoundException
        val domain = tokenCache.getDomain(id) ?: throw AuthorizedAccountNotFoundException

        val json = v1.getVerifyAccountsCredentials(domain.value, accessToken)

        accountCredentialCache.add(json)

        return json.toEntity(domain)
    }

    private fun AccountCredentialJson.toEntity(
        domain: Domain,
    ) = Account(
        AccountId(id),
        username,
        displayName,
        Uri(avatar),
        "@$username@${domain.value}",
    )
}
