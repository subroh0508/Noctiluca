package noctiluca.api.authentication.params

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface PostOauthToken {
    @Serializable
    data class Request(
        @SerialName("client_id")
        val clientId: String,
        @SerialName("client_secret")
        val clientSecret: String,
        @SerialName("redirect_uri")
        val redirectUri: String,
        val code: String,
        @SerialName("grant_type")
        val grantType: String = GRANT_TYPE,
    ) {
        companion object {
            private const val GRANT_TYPE = "authorization_code"
        }
    }
}
