package noctiluca.account.infra.repository

import noctiluca.model.AccountId
import noctiluca.model.Domain
import noctiluca.model.account.Account

interface AuthorizedAccountRepository {
    suspend fun getCurrent(): Pair<Account, Domain>?
    suspend fun getAll(): List<Account>

    suspend fun fetchCurrent(): Pair<Account, Domain>
    suspend fun refresh(id: AccountId): Account
}
