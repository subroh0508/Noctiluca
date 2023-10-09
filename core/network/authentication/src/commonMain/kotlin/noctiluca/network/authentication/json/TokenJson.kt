package noctiluca.network.authentication.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenJson(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("token_type")
    val tokenType: String,
    val scope: String,
    @SerialName("created_at")
    val createdAt: Long,
)
