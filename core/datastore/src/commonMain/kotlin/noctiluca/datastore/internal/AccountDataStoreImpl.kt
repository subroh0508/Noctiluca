package noctiluca.datastore.internal

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.first
import noctiluca.datastore.AccountDataStore
import noctiluca.model.AccountId
import noctiluca.model.account.Account

internal class AccountDataStoreImpl(
    private val dataStore: DataStore<List<SerializableAccount>>,
) : AccountDataStore {
    override suspend fun all() = dataStore.data.first().map { it.toEntity() }

    override suspend fun get(id: AccountId) = dataStore.data.first().find { it.id == id.value }?.toEntity()

    override suspend fun add(item: Account) = dataStore.updateData { list ->
        list.filterNot { it.url == item.url.value } + SerializableAccount(item)
    }.map { it.toEntity() }

    override suspend fun delete(id: AccountId) = dataStore.updateData { list ->
        list.filterNot { it.id == id.value }
    }.map { it.toEntity() }
}
