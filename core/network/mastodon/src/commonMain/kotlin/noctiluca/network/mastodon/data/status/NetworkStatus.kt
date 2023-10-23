package noctiluca.network.mastodon.data.status

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import noctiluca.network.mastodon.data.account.NetworkAccount
import noctiluca.network.mastodon.data.customemoji.NetworkCustomEmoji
import noctiluca.network.mastodon.data.filterresult.NetworkFilterResult
import noctiluca.network.mastodon.data.mediaattachment.NetworkMediaAttachment
import noctiluca.network.mastodon.data.poll.NetworkPoll
import noctiluca.network.mastodon.data.previewcard.NetworkPreviewCard

/**
 *  ref. https://docs.joinmastodon.org/entities/Status/
 */

@Serializable
data class NetworkStatus(
    val id: String,
    val uri: String,
    @SerialName("created_at")
    val createdAt: String,
    val account: NetworkAccount,
    val content: String,
    val visibility: String,
    val sensitive: Boolean,
    @SerialName("spoiler_text")
    val spoilerText: String,
    @SerialName("media_attachments")
    val mediaAttachments: List<NetworkMediaAttachment>,
    val application: Application?,
    val mentions: List<Mention>,
    val tags: List<Tag>,
    val emojis: List<NetworkCustomEmoji>,
    @SerialName("reblogs_count")
    val reblogsCount: Int,
    @SerialName("favourites_count")
    val favouritesCount: Int,
    @SerialName("replies_count")
    val repliesCount: Int,
    val url: String?,
    @SerialName("in_reply_to_id")
    val inReplyToId: String?,
    @SerialName("in_reply_to_account_id")
    val inReplyToAccountId: String?,
    val reblog: NetworkStatus?,
    val poll: NetworkPoll?,
    val card: NetworkPreviewCard?,
    val language: String?,
    val text: String?,
    @SerialName("edited_at")
    val editedAt: String?,
    val favourited: Boolean?,
    val reblogged: Boolean?,
    val muted: Boolean?,
    val bookmarked: Boolean?,
    val pinned: Boolean?,
    val filtered: List<NetworkFilterResult.Filter>?,
) {
    @Serializable
    data class Application(
        val name: String?,
        val website: String?,
    )

    @Serializable
    data class Mention(
        val id: String,
        val username: String,
        val url: String,
        val acct: String,
    )

    @Serializable
    data class Tag(
        val name: String,
        val url: String,
    )
}
