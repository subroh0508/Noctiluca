package noctiluca.account.model

import noctiluca.model.AccountId
import noctiluca.model.Hostname
import noctiluca.model.Uri

data class Account(
    val id: AccountId,
    val username: String,
    val displayName: String,
    val hostname: Hostname,
    val avatar: Uri,
) {
    val screen get() = "$username@${hostname.value}"
}
