package noctiluca.account.infra.repository.local

import noctiluca.model.AccountId
import noctiluca.model.Domain
import noctiluca.model.account.Account

interface LocalAuthorizedAccountRepository {
    suspend fun getCurrentAccount(): Account?
    suspend fun getCurrentDomain(): Domain?
    suspend fun getAll(): List<Account>

    suspend fun getAccessToken(id: AccountId): Pair<String, Domain>?

    suspend fun save(account: Account)
}
