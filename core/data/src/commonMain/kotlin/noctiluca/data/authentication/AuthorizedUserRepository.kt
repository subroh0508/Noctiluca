package noctiluca.data.authentication

import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser
import noctiluca.model.Uri

interface AuthorizedUserRepository {
    suspend fun fetch(
        code: String,
        redirectUri: Uri,
    ): AuthorizedUser?

    suspend fun switch(id: AccountId): AuthorizedUser

    suspend fun expireCurrent()
}
