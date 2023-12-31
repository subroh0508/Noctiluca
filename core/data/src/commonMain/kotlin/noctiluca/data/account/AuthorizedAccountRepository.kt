package noctiluca.data.account

import kotlinx.coroutines.flow.Flow
import noctiluca.model.AccountId
import noctiluca.model.Domain
import noctiluca.model.account.Account

interface AuthorizedAccountRepository {
    suspend fun current(): Flow<Pair<Account, Domain>?>
    suspend fun others(): Flow<List<Account>>

    suspend fun getCurrent(): Pair<Account, Domain>?

    suspend fun fetchAll(): List<Account>
    suspend fun fetchCurrent(): Pair<Account, Domain>
    suspend fun refresh(id: AccountId): Account
}
