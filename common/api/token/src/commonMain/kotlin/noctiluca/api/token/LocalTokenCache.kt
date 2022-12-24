package noctiluca.api.token

import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser
import noctiluca.model.Hostname
import noctiluca.repository.TokenCache

expect class LocalTokenCache : TokenCache {
    suspend fun getAll(): List<AuthorizedUser>

    suspend fun getCurrent(): AuthorizedUser?

    suspend fun setCurrent(id: AccountId): AuthorizedUser

    suspend fun add(id: AccountId, hostname: Hostname, accessToken: String): List<AuthorizedUser>

    suspend fun delete(id: AccountId): List<AuthorizedUser>
}