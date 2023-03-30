package noctiluca.accountdetail.model

import noctiluca.model.AccountId
import noctiluca.model.Uri

data class AccountDetail(
    val id: AccountId,
    val username: String,
    val displayName: String,
    val url: Uri,
    val avatar: Uri,
    val header: Uri,
    val screen: String,
    val note: String,
    val followersCount: Int,
    val followingCount: Int,
    val statusesCount: Int,
    val locked: Boolean,
    val bot: Boolean,
    val relationship: Set<Relationship>?,
    val condition: Condition?,
    val fields: List<Field>,
    val movedTo: AccountDetail?,
) {
    enum class Condition {
        LIMITED,
        SUSPENDED,
    }

    data class Field(
        val name: String,
        val value: String,
    )
}
