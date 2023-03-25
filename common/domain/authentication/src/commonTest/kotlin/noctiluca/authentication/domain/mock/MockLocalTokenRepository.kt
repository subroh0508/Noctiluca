package noctiluca.authentication.domain.mock

import noctiluca.api.authentication.json.AppCredentialJson
import noctiluca.authentication.infra.repository.local.LocalTokenRepository
import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser
import noctiluca.model.Domain
import noctiluca.test.model.MockAuthorizedUser

class MockLocalTokenRepository(
    private val currentAppCredential: Pair<Domain, AppCredentialJson>?,
    private val currentAuthorizedUser: AuthorizedUser?,
) : LocalTokenRepository {
    private val credentials: MutableList<AppCredentialJson> = mutableListOf()
    private val users: MutableList<AuthorizedUser> = mutableListOf()

    override suspend fun getCurrentAppCredential() = currentAppCredential
    override suspend fun saveAppCredential(
        domain: Domain,
        credential: AppCredentialJson,
    ) {
        credentials.add(credential)
    }

    override suspend fun getCurrentAuthorizedUser() = currentAuthorizedUser
    override suspend fun switchCurrentAuthorizedUser(id: AccountId) = currentAuthorizedUser!!
    override suspend fun expireCurrentAuthorizedUser() = Unit
    override suspend fun saveAuthorizedUser(
        id: AccountId,
        domain: Domain,
        accessToken: String,
    ) = MockAuthorizedUser(id, domain).also { mock -> users.add(mock) }
}
