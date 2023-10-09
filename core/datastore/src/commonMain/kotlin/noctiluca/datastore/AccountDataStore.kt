package noctiluca.datastore

import noctiluca.model.AccountId
import noctiluca.model.account.Account

internal expect class AccountDataStore {
    suspend fun get(id: AccountId): Account?
    suspend fun add(item: Account): List<Account>
    suspend fun delete(id: AccountId): List<Account>
}
