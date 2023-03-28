package noctiluca.accountdetail.infra.repository.impl

import noctiluca.accountdetail.infra.repository.AccountDetailRepository
import noctiluca.accountdetail.infra.toValueObject
import noctiluca.accountdetail.model.AccountDetail
import noctiluca.api.mastodon.MastodonApiV1
import noctiluca.api.mastodon.json.account.AccountJson
import noctiluca.model.AccountId
import noctiluca.model.Uri

internal class AccountDetailRepositoryImpl(
    private val v1: MastodonApiV1,
) : AccountDetailRepository {
    override suspend fun fetch(
        id: AccountId,
    ) = v1.getAccount(id.value).toEntity()

    private fun AccountJson.toEntity() = AccountDetail(
        AccountId(id),
        username,
        displayName,
        Uri(url),
        Uri(avatar),
        Uri(header),
        "@$acct",
        note,
        followersCount,
        followingCount,
        statusesCount,
        locked,
        bot,
        fields.map { it.toValueObject() },
    )
}
