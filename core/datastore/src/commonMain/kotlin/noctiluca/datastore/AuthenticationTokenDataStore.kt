package noctiluca.datastore

import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser
import noctiluca.model.Domain

interface AuthenticationTokenDataStore {
    suspend fun getCurrentAccessToken(): String?

    suspend fun getCurrentDomain(): String?

    suspend fun getAll(): List<AuthorizedUser>

    suspend fun getCurrent(): AuthorizedUser?

    suspend fun setCurrent(id: AccountId): AuthorizedUser

    suspend fun getAccessToken(id: AccountId): String?

    suspend fun getDomain(id: AccountId): Domain?

    suspend fun add(id: AccountId, domain: Domain, accessToken: String): List<AuthorizedUser>

    suspend fun delete(id: AccountId): List<AuthorizedUser>
}
