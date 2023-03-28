package noctiluca.account.infra.repository.impl

import noctiluca.account.infra.repository.AccountRepository
import noctiluca.model.Account
import noctiluca.api.mastodon.MastodonApiV1
import noctiluca.api.mastodon.json.account.AccountJson
import noctiluca.model.*

internal class AccountRepositoryImpl(
    private val v1: MastodonApiV1,
) : AccountRepository {
    override suspend fun fetchAccount(
        id: AccountId,
    ) = v1.getAccount(id.value).toEntity()

    private fun AccountJson.toEntity() = Account(
        AccountId(id),
        username,
        displayName,
        Uri(url),
        Uri(avatar),
        "@$acct",
    )
}
