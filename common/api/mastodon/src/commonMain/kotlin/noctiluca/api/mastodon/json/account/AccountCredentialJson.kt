package noctiluca.api.mastodon.json.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import noctiluca.api.mastodon.json.role.RoleJson

/**
 *  ref. https://docs.joinmastodon.org/entities/Account/#CredentialAccount
 */

@Serializable
data class AccountCredentialJson(
    val id: String,
    val username: String,
    val acct: String,
    @SerialName("display_name")
    val displayName: String,
    val note: String,
    val source: Source,
    val fields: List<FieldJson>,
    val role: RoleJson?, // Since 4.0.0
) {
    @Serializable
    data class Source(
        val privacy: String,
        val note: String,
        val sensitive: Boolean,
        val language: String,
        @SerialName("follow_requests_count")
        val followRequestsCount: Int,
        val fields: List<FieldJson>,
    )
}
