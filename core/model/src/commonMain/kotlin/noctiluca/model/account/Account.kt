package noctiluca.model.account

import noctiluca.model.AccountId
import noctiluca.model.Uri

data class Account(
    val id: AccountId,
    val username: String,
    val displayName: String,
    val url: Uri,
    val avatar: Uri,
    val screen: String,
)
