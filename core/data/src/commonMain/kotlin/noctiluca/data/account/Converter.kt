package noctiluca.data.account

import noctiluca.model.AccountId
import noctiluca.model.Domain
import noctiluca.model.Uri
import noctiluca.model.account.Account
import noctiluca.network.mastodon.data.account.NetworkAccount
import noctiluca.network.mastodon.data.account.NetworkAccountCredential

internal fun NetworkAccountCredential.toEntity(
    domain: Domain,
) = Account(
    AccountId(id),
    username,
    displayName,
    Uri(url),
    Uri(avatar),
    "@$username@${domain.value}",
)

internal fun NetworkAccount.toEntity(
    domain: Domain,
) = Account(
    AccountId(id),
    username,
    displayName,
    Uri(url),
    Uri(avatar),
    "@$username@${domain.value}",
)
