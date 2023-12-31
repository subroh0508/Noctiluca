package noctiluca.data.account

import kotlinx.coroutines.flow.Flow
import noctiluca.model.Domain
import noctiluca.model.account.Account

interface AuthorizedAccountRepository {
    suspend fun current(): Flow<Pair<Account, Domain>?>
    suspend fun others(): Flow<List<Account>>
}
