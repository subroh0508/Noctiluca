package noctiluca.network.mastodon.data.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import noctiluca.network.mastodon.data.customemoji.NetworkCustomEmoji
import noctiluca.network.mastodon.data.role.NetworkRole

/**
 *  ref. https://docs.joinmastodon.org/entities/Account/#CredentialAccount
 */

@Serializable
data class NetworkAccountCredential(
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
    val fields: List<NetworkField>,
    val emojis: List<NetworkCustomEmoji>,
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
    val source: Source,
    val role: NetworkRole?, // Since 4.0.0
) {
    @Serializable
    data class Source(
        val privacy: String,
        val note: String,
        val sensitive: Boolean,
        val language: String?,
        @SerialName("follow_requests_count")
        val followRequestsCount: Int,
        val fields: List<NetworkField>,
    )

    fun hasSameIdentifier(
        id: String,
        domain: String,
    ) = this.id == id && acct.endsWith("@$domain")
}
