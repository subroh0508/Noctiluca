package noctiluca.account.infra.repository.local

import kotlinx.serialization.json.Json
import noctiluca.api.mastodon.json.account.AccountCredentialJson
import noctiluca.model.AccountId
import noctiluca.preferences.JsonPreferences
import noctiluca.preferences.add
import noctiluca.preferences.remove
import java.util.prefs.Preferences

internal actual class LocalAccountCredentialCache(
    private val prefs: JsonPreferences<List<AccountCredentialJson>>,
) {
    constructor(json: Json) : this(
        JsonPreferences(
            json,
            listOf(),
            Preferences.userNodeForPackage(LocalAccountCredentialCache::class.java),
        ),
    )

    actual suspend fun get(id: AccountId) = prefs.data.find { it.id == id.value }
    actual suspend fun add(json: AccountCredentialJson) = prefs.add(json)
    actual suspend fun delete(id: AccountId) = get(id)?.let { prefs.remove(it) } ?: prefs.data
}
