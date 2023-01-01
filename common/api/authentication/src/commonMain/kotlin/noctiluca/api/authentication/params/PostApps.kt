package noctiluca.api.authentication.params

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

abstract class PostApps {
    companion object {
        private const val SCOPE = "read write follow push"
        private const val WEBSITE = "https://github.com/subroh0508/noctiluca"
        internal const val ESCAPED_SCOPE = "read%20write%20follow%20push"
    }

    @Serializable
    data class Request(
        @SerialName("client_name")
        val clientName: String,
        @SerialName("redirect_uris")
        val redirectUris: String,
        val scopes: String = SCOPE,
        val website: String = WEBSITE,
    )
}
