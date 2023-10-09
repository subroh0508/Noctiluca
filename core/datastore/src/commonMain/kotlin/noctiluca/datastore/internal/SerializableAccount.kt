package noctiluca.datastore.internal

import kotlinx.serialization.Serializable
import noctiluca.model.AccountId
import noctiluca.model.Uri
import noctiluca.model.account.Account

@Serializable
internal data class SerializableAccount(
    val id: String,
    val username: String,
    val displayName: String,
    val url: String,
    val avatar: String,
    val screen: String,
) {
    constructor(account: Account) : this(
        account.id.value,
        account.username,
        account.displayName,
        account.url.value,
        account.avatar.value,
        account.screen,
    )

    fun toEntity() = Account(
        AccountId(id),
        username,
        displayName,
        Uri(url),
        Uri(avatar),
        screen,
    )
}
