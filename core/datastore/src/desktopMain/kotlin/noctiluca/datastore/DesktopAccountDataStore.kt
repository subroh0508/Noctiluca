package noctiluca.datastore

import kotlinx.serialization.json.Json
import noctiluca.datastore.internal.SerializableAccount
import noctiluca.model.AccountId
import noctiluca.model.account.Account
import java.util.prefs.Preferences

internal class DesktopAccountDataStore private constructor(
    private val prefs: JsonPreferences<List<SerializableAccount>>,
) : AccountDataStore {
    constructor(json: Json) : this(
        JsonPreferences(
            json,
            listOf(),
            Preferences.userNodeForPackage(AccountDataStore::class.java),
        ),
    )

    override suspend fun all() = prefs.data.map { it.toEntity() }
    override suspend fun get(id: AccountId) = prefs.data.find { it.id == id.value }?.toEntity()
    override suspend fun add(item: Account) = prefs.add(SerializableAccount(item)).map { it.toEntity() }
    override suspend fun delete(id: AccountId) =
        (get(id)?.let { prefs.remove(SerializableAccount(it)) } ?: prefs.data).map { it.toEntity() }
}
