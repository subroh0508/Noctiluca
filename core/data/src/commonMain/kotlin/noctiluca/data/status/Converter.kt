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
    if (reblog != null && accountId?.value != reblog?.account?.id) {
        reblog?.account?.toTooter()
    } else {
        null
    },
    (reblog?.application ?: application)?.let { Status.Via(it.name, it.website?.let(::Uri)) },
    (reblog?.mediaAttachments ?: mediaAttachments).map { it.toEntity() },
)

private fun NetworkAccount.toTooter() = Account(
    AccountId(id),
    username,
    displayName,
    Uri(url),
    Uri(avatar),
    "@$acct",
)

private enum class AttachmentType { IMAGE, GIFV, VIDEO, AUDIO, UNKNOWN }

private fun NetworkMediaAttachment.toEntity() = when (AttachmentType.valueOf(type.uppercase())) {
    AttachmentType.IMAGE -> Attachment.Image(
        url = Uri(url),
        previewUrl = Uri(previewUrl),
        description = description,
    )

    AttachmentType.GIFV -> Attachment.Gifv(
        url = Uri(url),
        previewUrl = Uri(previewUrl),
        description = description,
    )

    AttachmentType.VIDEO -> Attachment.Video(
        url = Uri(url),
        previewUrl = Uri(previewUrl),
        description = description,
    )

    AttachmentType.AUDIO -> Attachment.Audio(
        url = Uri(url),
        previewUrl = Uri(previewUrl),
        description = description,
    )

    AttachmentType.UNKNOWN -> Attachment.Unknown(
        url = Uri(url),
        previewUrl = Uri(previewUrl),
        description = description,
    )
}
