package noctiluca.authentication.infra.repository.local

import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser
import noctiluca.model.Domain
import noctiluca.network.authentication.json.AppCredentialJson

interface LocalTokenRepository {
    suspend fun getCurrentAppCredential(): Pair<Domain, AppCredentialJson>?
    suspend fun saveAppCredential(
        domain: Domain,
        credential: AppCredentialJson,
    )

    suspend fun getCurrentAuthorizedUser(): AuthorizedUser?
    suspend fun switchCurrentAuthorizedUser(id: AccountId): AuthorizedUser
    suspend fun expireCurrentAuthorizedUser()
    suspend fun saveAuthorizedUser(
        id: AccountId,
        domain: Domain,
        accessToken: String,
    ): AuthorizedUser
}
