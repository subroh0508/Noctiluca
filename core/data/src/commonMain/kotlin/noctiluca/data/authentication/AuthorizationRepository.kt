package noctiluca.data.authentication

import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser
import noctiluca.model.Domain
import noctiluca.model.Uri

interface AuthorizationRepository {
    suspend fun fetchAuthorizeUrl(
        domain: Domain,
        client: String,
        redirectUri: Uri,
    ): Uri

    suspend fun fetchAccessToken(
        code: String,
        redirectUri: Uri,
    ): AuthorizedUser?

    suspend fun switchAccessToken(id: AccountId): AuthorizedUser
}
