package noctiluca.account.infra.repository

import noctiluca.account.model.Account
import noctiluca.model.AuthorizedUser

interface AccountRepository {
    suspend fun fetchCurrentAuthorizedAccount(): Account
    suspend fun fetchAuthorizedAccount(user: AuthorizedUser): Account
}
