package noctiluca.authentication.infra.repository.local

import noctiluca.api.authentication.json.AppCredentialJson
import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser
import noctiluca.model.Domain

interface LocalTokenRepository {
    suspend fun getCurrentAppCredential(): Pair<Domain, AppCredentialJson>?
    suspend fun saveAppCredential(
        domain: Domain,
        credential: AppCredentialJson,
    )

    suspend fun getCurrentAuthorizedUser(): AuthorizedUser?
    suspend fun switchCurrentAuthorizedUser(id: AccountId): AuthorizedUser
    suspend fun saveAuthorizedUser(
        id: AccountId,
        domain: Domain,
        accessToken: String,
    ): AuthorizedUser

    suspend fun expireAuthorizedUser()
}
