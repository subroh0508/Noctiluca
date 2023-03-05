package noctiluca.account.infra

import noctiluca.account.model.Account
import noctiluca.api.mastodon.json.account.AccountCredentialJson
import noctiluca.model.AccountId
import noctiluca.model.Domain
import noctiluca.model.Uri

internal fun AccountCredentialJson.toEntity(
    domain: Domain,
) = Account(
    AccountId(id),
    username,
    displayName,
    Uri(avatar),
    "@$username@${domain.value}",
)
