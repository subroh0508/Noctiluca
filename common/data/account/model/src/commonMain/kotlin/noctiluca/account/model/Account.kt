package noctiluca.account.model

import noctiluca.model.AccountId

data class Account(
    val id: AccountId,
    val username: String,
    val displayName: String,
    val hostname: String,
    val avatar: String,
) {
    val screen get() = "$username@${hostname}"
}
