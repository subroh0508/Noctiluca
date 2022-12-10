package noctiluca.api.authentication.params

import kotlinx.serialization.Serializable

abstract class GetAccountsVerifyCredential {
    @Serializable
    data class Response(
        val id: String,
        val hostname: String,
    )
}
