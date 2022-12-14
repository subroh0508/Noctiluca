package noctiluca.api.mastodon.json.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import noctiluca.api.mastodon.json.customemoji.CustomEmojiJson

/**
 *  ref. https://docs.joinmastodon.org/entities/Account/
 */

@Serializable
data class AccountJson(
    val id: String,
    val username: String,
    val acct: String,
    val url: String,
    @SerialName("display_name")
    val displayName: String,
    val note: String,
    val avatar: String,
    @SerialName("avatar_static")
    val avatarStatic: String,
    val header: String,
    @SerialName("header_static")
    val headerStatic: String,
    val locked: Boolean,
    val fields: List<FieldJson>,
    val emojis: List<CustomEmojiJson>,
    val bot: Boolean,
    val group: Boolean,
    val discoverable: Boolean?,
    val noindex: Boolean?, // Since 4.0.0
    val moved: Boolean?,
    val suspended: Boolean?,
    val limited: Boolean?,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("last_status_at")
    val lastStatusAt: String?, // 3.0.0 <= With Time / 3.1.0 <= Only Date
    @SerialName("statuses_count")
    val statusesCount: Int,
    @SerialName("followers_count")
    val followersCount: Int,
    @SerialName("following_count")
    val followingCount: Int,
)
