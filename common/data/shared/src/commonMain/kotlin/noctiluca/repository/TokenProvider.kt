package noctiluca.repository

import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser

interface TokenProvider {
    suspend fun getCurrent(): AuthorizedUser?

    suspend fun getAuthorizedUser(): List<AuthorizedUser>

    suspend fun switch(id: AccountId): AuthorizedUser

    suspend fun expireCurrent()
}
