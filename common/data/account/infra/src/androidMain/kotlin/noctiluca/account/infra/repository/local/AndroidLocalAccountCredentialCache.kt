package noctiluca.account.infra.repository.local

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.first
import noctiluca.api.mastodon.json.account.AccountCredentialJson
import noctiluca.model.AccountId
import noctiluca.model.Domain

internal actual class LocalAccountCredentialCache(
    private val dataStore: DataStore<List<AccountCredentialJson>>,
) {
    actual suspend fun get(
        id: AccountId,
        domain: Domain,
    ) = dataStore.data.first().find {
        it.hasSameIdentifier(id.value, domain.value)
    }

    actual suspend fun add(json: AccountCredentialJson) = dataStore.updateData { list ->
        list.filterNot { it.url == json.url } + json
    }

    actual suspend fun delete(
        id: AccountId,
        domain: Domain,
    ) = dataStore.updateData { list ->
        list.filterNot { it.hasSameIdentifier(id.value, domain.value) }
    }
}
