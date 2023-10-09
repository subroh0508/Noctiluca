package noctiluca.data.account

import noctiluca.model.AccountId
import noctiluca.model.Domain
import noctiluca.model.Uri
import noctiluca.model.account.Account
import noctiluca.network.mastodon.json.account.AccountCredentialJson

internal fun AccountCredentialJson.toEntity(
    domain: Domain,
) = Account(
    AccountId(id),
    username,
    displayName,
    Uri(url),
    Uri(avatar),
    "@$username@${domain.value}",
)
