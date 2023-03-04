package noctiluca.account.infra.repository

import noctiluca.account.model.Account
import noctiluca.model.AccountId

interface AccountRepository {
    suspend fun fetchAccount(id: AccountId): Account
}
