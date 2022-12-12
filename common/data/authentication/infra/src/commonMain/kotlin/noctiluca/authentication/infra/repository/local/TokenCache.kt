package noctiluca.authentication.infra.repository.local

import noctiluca.authentication.infra.internal.Token
import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser

internal expect class TokenCache {
    suspend fun getAll(): List<AuthorizedUser>

    suspend fun getCurrent(): AuthorizedUser?

    suspend fun setCurrent(id: AccountId): AuthorizedUser

    suspend fun add(token: Token): List<AuthorizedUser>

    suspend fun delete(id: AccountId): List<AuthorizedUser>
}
