package noctiluca.data.mock

import noctiluca.datastore.AccountDataStore
import noctiluca.model.AccountId
import noctiluca.model.account.Account

fun buildEmptyMockAccountDataStore() = MockAccountDataStore(emptyList())

class MockAccountDataStore(init: List<Account>) : AccountDataStore {
    private var accounts = init

    override suspend fun get(id: AccountId) = accounts.find { it.id == id }
    override suspend fun add(item: Account): List<Account> {
        accounts = accounts.filter { it.id != item.id } + item
        return accounts
    }

    override suspend fun delete(id: AccountId): List<Account> {
        accounts = accounts.filter { it.id != id }
        return accounts
    }
}
