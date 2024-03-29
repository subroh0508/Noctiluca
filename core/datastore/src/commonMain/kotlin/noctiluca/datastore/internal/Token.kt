package noctiluca.datastore.internal

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser
import noctiluca.model.Domain

internal data class Token(
    override val id: AccountId,
    override val domain: Domain,
    val accessToken: String,
) : AuthorizedUser {
    internal constructor(json: Json) : this(
        AccountId(json.accountId),
        Domain(json.domain),
        json.accessToken,
    )

    @Serializable
    internal data class Json(
        @SerialName("account_id")
        val accountId: String,
        val domain: String,
        @SerialName("access_token")
        val accessToken: String,
        val current: Boolean,
    ) {
        constructor(
            id: AccountId,
            domain: Domain,
            accessToken: String,
        ) : this(
            id.value,
            domain.value,
            accessToken,
            current = false,
        )
    }
}
