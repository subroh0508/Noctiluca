package noctiluca.data.account

import kotlinx.coroutines.flow.Flow
import noctiluca.model.AccountId
import noctiluca.model.Domain
import noctiluca.model.account.Account

interface AuthorizedAccountRepository {
    fun current(): Flow<Pair<Account, Domain>?>
    fun others(): Flow<List<Account>>
    suspend fun switch(id: AccountId)
}
