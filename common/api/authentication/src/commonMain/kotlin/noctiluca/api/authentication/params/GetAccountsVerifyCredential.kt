package noctiluca.api.authentication.params

import kotlinx.serialization.Serializable

sealed interface GetAccountsVerifyCredential {
    @Serializable
    data class Response(
        val id: String,
    )
}
