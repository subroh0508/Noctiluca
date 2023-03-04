package noctiluca.status.model

import noctiluca.model.AccountId
import noctiluca.model.Uri

data class Tooter(
    val id: AccountId,
    val username: String,
    val displayName: String,
    val url: Uri,
    val avatar: Uri,
    val screen: String,
)
