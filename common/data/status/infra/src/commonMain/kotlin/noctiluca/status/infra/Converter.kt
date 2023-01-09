package noctiluca.status.infra

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import noctiluca.api.mastodon.json.account.AccountJson
import noctiluca.api.mastodon.json.status.StatusJson
import noctiluca.model.AccountId
import noctiluca.model.StatusId
import noctiluca.model.Uri
import noctiluca.status.model.Status
import noctiluca.status.model.Tooter

fun StatusJson.toEntity(newAccountId: AccountId?) = Status(
    (reblog?.id ?: id).let(::StatusId),
    reblog?.content ?: content,
    spoilerText,
    (reblog?.createdAt ?: createdAt).toInstant().toLocalDateTime(TimeZone.of("Asia/Tokyo")),
    Status.Visibility.valueOf(visibility.uppercase()),
    reblog?.repliesCount ?: repliesCount,
    reblog?.favouritesCount ?: favouritesCount,
    reblog?.reblogsCount ?: reblogsCount,
    (reblog?.favourited ?: favourited) ?: false,
    (reblog?.reblogged ?: reblogged) ?: false,
    (reblog?.account ?: account).toTooter(),
    if (reblog != null && newAccountId?.value != reblog?.account?.id)
        reblog?.account?.toTooter()
    else
        null,
)

private fun AccountJson.toTooter() = Tooter(
    AccountId(id),
    acct,
    displayName,
    Uri(url),
    Uri(avatar),
)
