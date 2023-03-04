package noctiluca.account.infra.repository.local

import kotlinx.serialization.json.Json
import noctiluca.api.mastodon.json.account.AccountCredentialJson
import noctiluca.model.AccountId
import noctiluca.model.Domain
import java.util.prefs.Preferences

internal actual class LocalAccountCredentialCache(
    private val json: Json,
    private val prefs: Preferences,
) {
    actual suspend fun get(id: AccountId): AccountCredentialJson? = null
    actual suspend fun add(json: AccountCredentialJson): List<AccountCredentialJson> = listOf()
    actual suspend fun delete(id: AccountId): List<AccountCredentialJson> = listOf()
}
