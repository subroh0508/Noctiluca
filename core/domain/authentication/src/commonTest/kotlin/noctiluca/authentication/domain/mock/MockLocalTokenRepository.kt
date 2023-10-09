package noctiluca.authentication.domain.mock

import noctiluca.network.authentication.json.AppCredentialJson
import noctiluca.authentication.infra.repository.local.LocalTokenRepository
import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser
import noctiluca.model.Domain
import noctiluca.test.model.MockAuthorizedUser

class MockLocalTokenRepository(
    private val currentAppCredential: Pair<Domain, AppCredentialJson>? = null,
    private val currentAuthorizedUser: AuthorizedUser? = null,
) : LocalTokenRepository {
    private val _credentials: MutableList<AppCredentialJson> = mutableListOf()
    private val _users: MutableList<AuthorizedUser> = mutableListOf()

    val credentials get() = _credentials
    val users get() = _users

    override suspend fun getCurrentAppCredential() = currentAppCredential
    override suspend fun saveAppCredential(
        domain: Domain,
        credential: AppCredentialJson,
    ) {
        _credentials.add(credential)
    }

    override suspend fun getCurrentAuthorizedUser() = currentAuthorizedUser
    override suspend fun switchCurrentAuthorizedUser(id: AccountId) = currentAuthorizedUser!!
    override suspend fun expireCurrentAuthorizedUser() = Unit
    override suspend fun saveAuthorizedUser(
        id: AccountId,
        domain: Domain,
        accessToken: String,
    ) = MockAuthorizedUser(id, domain).also { mock -> _users.add(mock) }
}
