package noctiluca.account.infra.repository.impl

import noctiluca.account.infra.repository.AccountRepository
import noctiluca.account.model.Account
import noctiluca.api.mastodon.MastodonV1Api
import noctiluca.api.mastodon.json.account.AccountCredentialJson
import noctiluca.model.*
import noctiluca.repository.TokenProvider

internal class AccountRepositoryImpl(
    private val tokenProvider: TokenProvider,
    private val v1: MastodonV1Api,
) : AccountRepository {
    override suspend fun fetchCurrentAccount(): Account {
        val current = tokenProvider.getCurrent() ?: throw AuthorizedTokenNotFoundException

        return v1.getVerifyAccountsCredentials(current.hostname.value).toEntity(current.hostname)
    }

    override suspend fun fetchAllAuthorizedAccounts(): List<Account> {
        val accounts = tokenProvider.getAuthorizedUsers().takeIf(List<AuthorizedUser>::isNotEmpty) ?: throw AuthorizedTokenNotFoundException

        return accounts.map {
            v1.getVerifyAccountsCredentials(it.hostname.value).toEntity(it.hostname)
        }
    }

    private fun AccountCredentialJson.toEntity(hostname: Hostname) = Account(
        AccountId(id),
        username,
        displayName,
        hostname,
        Uri(avatar),
    )
}