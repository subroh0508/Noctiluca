package noctiluca.data.account

import kotlinx.coroutines.flow.Flow
import noctiluca.model.account.Account

interface AuthorizedAccountRepository {
    fun all(): Flow<List<Account>>
}
