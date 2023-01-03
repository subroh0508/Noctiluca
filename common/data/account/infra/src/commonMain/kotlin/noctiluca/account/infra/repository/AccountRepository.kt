package noctiluca.account.infra.repository

import noctiluca.account.model.Account

interface AccountRepository {
    suspend fun fetchCurrentAccount(): Account
    suspend fun fetchAllAuthorizedAccounts(): List<Account>
}