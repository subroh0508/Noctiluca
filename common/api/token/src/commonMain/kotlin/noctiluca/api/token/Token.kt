package noctiluca.api.token

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser
import noctiluca.model.Hostname

data class Token(
    override val id: AccountId,
    override val hostname: Hostname,
    val accessToken: String,
) : AuthorizedUser {
    internal constructor(json: Json) : this(
        AccountId(json.accountId),
        Hostname(json.hostname),
        json.accessToken,
    )

    interface Cache {
        suspend fun getCurrentAccessToken(): String?
    }

    @Serializable
    internal data class Json(
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
}
