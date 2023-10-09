package noctiluca.data.authentication

import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser
import noctiluca.model.Uri

interface AuthorizedUserRepository {
    suspend fun fetchAccessToken(
        code: String,
        redirectUri: Uri,
    ): String?

    suspend fun getCurrent(): AuthorizedUser?

    suspend fun switch(id: AccountId): AuthorizedUser

    suspend fun expireCurrent()
}
