package noctiluca.account.infra.repository

import noctiluca.account.model.Account
import noctiluca.model.AccountId
import noctiluca.model.Domain

interface AuthorizedAccountRepository {
    suspend fun fetchCurrent(): Account
    suspend fun refresh(id: AccountId, domain: Domain): Account
}
