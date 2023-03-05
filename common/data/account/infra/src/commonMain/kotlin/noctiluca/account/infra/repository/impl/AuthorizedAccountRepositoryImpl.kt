package noctiluca.account.infra.repository.impl

import noctiluca.account.infra.repository.AuthorizedAccountRepository
import noctiluca.account.infra.repository.local.LocalAuthorizedAccountRepository
import noctiluca.account.infra.toEntity
import noctiluca.account.model.Account
import noctiluca.api.mastodon.MastodonApiV1
import noctiluca.model.*

internal class AuthorizedAccountRepositoryImpl(
    private val local: LocalAuthorizedAccountRepository,
    private val v1: MastodonApiV1,
) : AuthorizedAccountRepository {
    override suspend fun getCurrent() = local.getCurrent()

    override suspend fun getAll() = local.getAll()

    override suspend fun fetchCurrent(): Pair<Account, Domain> {
        val (_, domain) = local.getCurrent() ?: throw AuthorizedAccountNotFoundException

        val json = runCatching {
            v1.getVerifyAccountsCredentials(domain.value)
        }.getOrNull() ?: throw AuthorizedAccountNotFoundException

        local.save(json)

        return json.toEntity(domain) to domain
    }

    override suspend fun refresh(id: AccountId): Account {
        val (accessToken, domain) = local.getAccessToken(id) ?: throw AuthorizedAccountNotFoundException
        val json = v1.getVerifyAccountsCredentials(domain.value, accessToken)

        local.save(json)

        return json.toEntity(domain)
    }
}
