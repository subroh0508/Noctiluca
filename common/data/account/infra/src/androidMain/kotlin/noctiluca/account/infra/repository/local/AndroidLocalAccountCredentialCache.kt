package noctiluca.account.infra.repository.local

import android.content.Context
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import noctiluca.api.mastodon.json.account.AccountCredentialJson
import noctiluca.datastore.JsonSerializer
import noctiluca.datastore.getJsonDataStore
import noctiluca.model.AccountId

internal actual class LocalAccountCredentialCache(
    private val dataStore: DataStore<List<AccountCredentialJson>>,
) {
    constructor(context: Context, json: Json) : this(
        context.getJsonDataStore(
            JsonSerializer(json, listOf()),
            LocalAccountCredentialCache::class.simpleName ?: "",
        )
    )

    actual suspend fun get(id: AccountId) = dataStore.data.first().find { it.id == id.value }

    actual suspend fun add(json: AccountCredentialJson) = dataStore.updateData { list ->
        list.filterNot { it.url == json.url } + json
    }

    actual suspend fun delete(id: AccountId) = dataStore.updateData { list ->
        list.filterNot { it.id == id.value }
    }
}
