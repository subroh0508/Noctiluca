package noctiluca.authentication.infra.repository

import noctiluca.authentication.model.AppCredential
import noctiluca.model.AuthorizedUser
import noctiluca.model.Domain
import noctiluca.model.Uri
import noctiluca.repository.TokenProvider

interface TokenRepository : TokenProvider {
    suspend fun fetchAppCredential(
        domain: Domain,
        client: String,
        redirectUri: Uri,
    ): AppCredential

    suspend fun cacheAppCredential(appCredential: AppCredential)

    suspend fun fetchToken(
        code: String,
        redirectUri: Uri,
    ): AuthorizedUser?
}