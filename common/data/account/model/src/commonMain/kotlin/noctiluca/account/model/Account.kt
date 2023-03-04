package noctiluca.account.model

import noctiluca.model.AccountId
import noctiluca.model.Domain
import noctiluca.model.Uri

data class Account(
    val id: AccountId,
    val username: String,
    val displayName: String,
    val avatar: Uri,
    val screen: String,
)
