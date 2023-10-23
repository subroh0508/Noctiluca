package noctiluca.data.status

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import noctiluca.model.AccountId
import noctiluca.model.StatusId
import noctiluca.model.Uri
import noctiluca.model.account.Account
import noctiluca.model.status.Status
import noctiluca.network.mastodon.data.account.NetworkAccount
import noctiluca.network.mastodon.data.status.NetworkStatus

@Suppress("CyclomaticComplexMethod")
fun NetworkStatus.toEntity(accountId: AccountId?) = Status(
    (reblog?.id ?: id).let(::StatusId),
    reblog?.content ?: content,
    spoilerText.takeIf(String::isNotBlank),
    (reblog?.createdAt ?: createdAt).toInstant().toLocalDateTime(TimeZone.of("Asia/Tokyo")),
    Status.Visibility.valueOf(visibility.uppercase()),
    reblog?.repliesCount ?: repliesCount,
    reblog?.favouritesCount ?: favouritesCount,
    reblog?.reblogsCount ?: reblogsCount,
    (reblog?.favourited ?: favourited) ?: false,
    (reblog?.reblogged ?: reblogged) ?: false,
    (reblog?.bookmarked ?: bookmarked) ?: false,
    (reblog?.account ?: account).toTooter(),
    if (reblog != null && accountId?.value != reblog?.account?.id) {
        reblog?.account?.toTooter()
    } else {
        null
    },
)

private fun NetworkAccount.toTooter() = Account(
    AccountId(id),
    username,
    displayName,
    Uri(url),
    Uri(avatar),
    "@$acct",
)
