package noctiluca.data.accountdetail

import kotlinx.coroutines.flow.Flow
import noctiluca.model.AccountId
import noctiluca.model.accountdetail.AccountAttributes

interface AccountDetailRepository {
    fun attributes(id: AccountId): Flow<AccountAttributes>
}
