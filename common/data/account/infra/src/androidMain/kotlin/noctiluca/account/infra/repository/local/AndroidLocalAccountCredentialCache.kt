package noctiluca.account.infra.repository.local

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.first
import noctiluca.api.mastodon.json.account.AccountCredentialJson
import noctiluca.model.AccountId

internal class AndroidLocalAccountCredentialCache(
    private val dataStore: DataStore<List<AccountCredentialJson>>,
) : LocalAccountCredentialCache {
    override suspend fun get(id: AccountId) = dataStore.data.first().find { it.id == id.value }

    override suspend fun add(json: AccountCredentialJson) = dataStore.updateData { list ->
        list.filterNot { it.url == json.url } + json
    }

    override suspend fun delete(id: AccountId) = dataStore.updateData { list ->
        list.filterNot { it.id == id.value }
    }
}
