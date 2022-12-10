package noctiluca.account.model

import noctiluca.model.AccountId

data class Account(
    val id: AccountId,
    val username: String,
    val displayName: String,
    val hostName: String,
    val avatar: String,
) {
    val screen get() = "$username@${hostName}"
}
