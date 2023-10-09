package noctiluca.network.mastodon.json.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 *  ref. https://docs.joinmastodon.org/entities/Account/#Field
 */
@Serializable
data class FieldJson(
    val name: String,
    val value: String,
    @SerialName("verified_at")
    val verifiedAt: String?,
)
