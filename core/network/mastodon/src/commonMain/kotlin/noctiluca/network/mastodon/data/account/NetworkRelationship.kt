package noctiluca.network.mastodon.data.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkRelationship(
    val id: String,
    val following: Boolean,
    @SerialName("showing_reblogs")
    val showingReblogs: Boolean,
    val notifying: Boolean,
    @SerialName("followed_by")
    val followedBy: Boolean,
    val blocking: Boolean,
    @SerialName("blocked_by")
    val blockedBy: Boolean,
    val muting: Boolean,
    @SerialName("muting_notifications")
    val mutingNotifications: Boolean,
    val requested: Boolean,
    @SerialName("domain_blocking")
    val domainBlocking: Boolean,
    val endorsed: Boolean,
    val note: String,
)
