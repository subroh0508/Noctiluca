package noctiluca.network.mastodon.data.role

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 *  ref. https://docs.joinmastodon.org/entities/Role/
 */
@Serializable
data class NetworkRole(
    val id: String,
    val name: String,
    val color: String,
    val position: Int?,
    val permissions: Int,
    val highlighted: Boolean,
    @SerialName("created_at")
    val createdAt: String?,
    @SerialName("updated_at")
    val updatedAt: String?,
)
