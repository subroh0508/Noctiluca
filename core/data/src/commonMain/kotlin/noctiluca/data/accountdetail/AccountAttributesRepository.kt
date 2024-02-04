package noctiluca.data.accountdetail

import kotlinx.coroutines.flow.Flow
import noctiluca.model.AccountId
import noctiluca.model.accountdetail.AccountAttributes

interface AccountAttributesRepository {
    fun attributes(id: AccountId): Flow<AccountAttributes>
}
