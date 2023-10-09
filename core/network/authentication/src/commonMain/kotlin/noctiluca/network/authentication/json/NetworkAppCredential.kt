package noctiluca.network.authentication.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkAppCredential(
    @SerialName("client_id")
    val clientId: String,
    @SerialName("client_secret")
    val clientSecret: String,
)
