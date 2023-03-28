package noctiluca.accountdetail.infra.repository

import noctiluca.accountdetail.model.AccountDetail
import noctiluca.model.AccountId

interface AccountDetailRepository {
    suspend fun fetchAccount(id: AccountId): AccountDetail
}
