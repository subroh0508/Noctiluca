package noctiluca.api.token

import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser
import noctiluca.model.Domain
import noctiluca.repository.TokenCache

expect class LocalTokenCache : TokenCache {
    suspend fun getAll(): List<AuthorizedUser>

    suspend fun getCurrent(): AuthorizedUser?

    suspend fun setCurrent(id: AccountId, domain: Domain): AuthorizedUser

    suspend fun getAccessToken(id: AccountId, domain: Domain): String?

    suspend fun add(id: AccountId, domain: Domain, accessToken: String): List<AuthorizedUser>

    suspend fun delete(id: AccountId, domain: Domain): List<AuthorizedUser>
}
