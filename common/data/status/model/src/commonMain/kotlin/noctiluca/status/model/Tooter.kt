package noctiluca.status.model

import noctiluca.model.AccountId

data class Tooter(
    val id: AccountId,
    val username: String,
    val displayName: String,
    val url: String,
    val avatar: String,
    val avatarStatic: String,
)
