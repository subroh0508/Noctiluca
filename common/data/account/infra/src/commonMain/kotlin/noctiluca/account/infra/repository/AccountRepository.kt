package noctiluca.account.infra.repository

import noctiluca.model.Account
import noctiluca.model.AccountId

interface AccountRepository {
    suspend fun fetchAccount(id: AccountId): Account
}
