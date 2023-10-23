package noctiluca.network.mastodon.data.extendeddescription

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkExtendedDescription(
    @SerialName("updated_at")
    val updatedAt: String,
    val content: String,
)
