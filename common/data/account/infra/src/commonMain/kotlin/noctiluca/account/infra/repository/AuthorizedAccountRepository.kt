package noctiluca.account.infra.repository

import noctiluca.account.model.Account
import noctiluca.model.AccountId
import noctiluca.model.Domain

interface AuthorizedAccountRepository {
    suspend fun getCurrent(): Pair<Account, Domain>?
    suspend fun getAll(): List<Account>

    suspend fun fetchCurrent(): Pair<Account, Domain>
    suspend fun refresh(id: AccountId): Account
}
