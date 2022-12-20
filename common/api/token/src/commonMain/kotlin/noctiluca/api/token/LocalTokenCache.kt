package noctiluca.api.token

import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser

expect class LocalTokenCache : Token.Cache {
    suspend fun getAll(): List<AuthorizedUser>

    suspend fun getCurrent(): AuthorizedUser?

    suspend fun setCurrent(id: AccountId): AuthorizedUser

    suspend fun add(token: Token): List<AuthorizedUser>

    suspend fun delete(id: AccountId): List<AuthorizedUser>
}