package noctiluca.data.status

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import noctiluca.model.AccountId
import noctiluca.model.StatusId
import noctiluca.model.Uri
import noctiluca.model.account.Account
import noctiluca.model.status.Attachment
import noctiluca.model.status.Status
import noctiluca.network.mastodon.data.account.NetworkAccount
import noctiluca.network.mastodon.data.mediaattachment.NetworkMediaAttachment
import noctiluca.network.mastodon.data.status.NetworkStatus

@Suppress("CyclomaticComplexMethod")
fun NetworkStatus.toEntity(accountId: AccountId?) = Status(
    (reblog?.id ?: id).let(::StatusId),
    reblog?.content ?: content,
    spoilerText.takeIf(String::isNotBlank),
    (reblog?.createdAt ?: createdAt).toInstant().toLocalDateTime(TimeZone.of("Asia/Tokyo")),
    Status.Visibility.valueOf(visibility.uppercase()),
    reblog?.sensitive ?: sensitive,
    reblog?.repliesCount ?: repliesCount,
    reblog?.favouritesCount ?: favouritesCount,
    reblog?.reblogsCount ?: reblogsCount,
    (reblog?.favourited ?: favourited) ?: false,
    (reblog?.reblogged ?: reblogged) ?: false,
    (reblog?.bookmarked ?: bookmarked) ?: false,
    (reblog?.account ?: account).toTooter(),
    if (reblog != null) {
        account.toTooter()
    } else {
        null
    },
    (reblog?.application ?: application)?.let { Status.Via(it.name, it.website?.let(::Uri)) },
    (reblog?.mediaAttachments ?: mediaAttachments).map { it.toEntity() },
)

internal fun NetworkMediaAttachment.toEntity() = when (AttachmentType.valueOf(type.uppercase())) {
    AttachmentType.IMAGE -> Attachment.Image(
        id = id,
        url = Uri(url),
        previewUrl = Uri(previewUrl),
        description = description,
    )

    AttachmentType.GIFV -> Attachment.Gifv(
        id = id,
        url = Uri(url),
        previewUrl = Uri(previewUrl),
        description = description,
    )

    AttachmentType.VIDEO -> Attachment.Video(
        id = id,
        url = Uri(url),
        previewUrl = Uri(previewUrl),
        description = description,
    )

    AttachmentType.AUDIO -> Attachment.Audio(
        id = id,
        url = Uri(url),
        previewUrl = Uri(previewUrl),
        description = description,
    )

    AttachmentType.UNKNOWN -> Attachment.Unknown(
        id = id,
        url = Uri(url),
        previewUrl = Uri(previewUrl),
        description = description,
    )
}

private fun NetworkAccount.toTooter() = Account(
    AccountId(id),
    username,
    displayName,
    Uri(url),
    Uri(avatar),
    "@$acct",
)

private enum class AttachmentType { IMAGE, GIFV, VIDEO, AUDIO, UNKNOWN }
