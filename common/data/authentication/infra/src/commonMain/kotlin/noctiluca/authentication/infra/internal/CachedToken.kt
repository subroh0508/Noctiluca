package noctiluca.authentication.infra.internal

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CachedToken(
    @SerialName("account_id")
    val accountId: String,
    @SerialName("hostname")
    val hostname: String,
    @SerialName("access_token")
    val accessToken: String,
    val current: Boolean,
) {
    constructor(token: Token) : this(
        token.id.value,
        token.hostname.value,
        token.accessToken,
        current = false,
    )
}
