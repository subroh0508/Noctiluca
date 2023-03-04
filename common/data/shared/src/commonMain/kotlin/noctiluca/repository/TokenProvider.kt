package noctiluca.repository

import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser
import noctiluca.model.Domain

interface TokenProvider {
    suspend fun getCurrent(): AuthorizedUser?

    suspend fun getAuthorizedUsers(includeCurrent: Boolean): List<AuthorizedUser>

    suspend fun switch(id: AccountId, domain: Domain): AuthorizedUser

    suspend fun expireCurrent()
}
