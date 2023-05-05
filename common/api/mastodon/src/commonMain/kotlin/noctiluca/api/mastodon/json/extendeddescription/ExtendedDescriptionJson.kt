package noctiluca.api.mastodon.json.extendeddescription

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExtendedDescriptionJson(
    @SerialName("updated_at")
    val updatedAt: String,
    val content: String,
)
