package noctiluca.authentication.infra.repository.local.impl

import noctiluca.network.authentication.json.AppCredentialJson
import noctiluca.api.token.LocalTokenCache
import noctiluca.authentication.infra.repository.local.AppCredentialCache
import noctiluca.authentication.infra.repository.local.LocalTokenRepository
import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser
import noctiluca.model.Domain

internal class LocalTokenRepositoryImpl(
    private val appCredentialCache: AppCredentialCache,
    private val tokenCache: LocalTokenCache,
) : LocalTokenRepository {
    override suspend fun getCurrentAppCredential() = appCredentialCache.getCurrent()

    override suspend fun saveAppCredential(
        domain: Domain,
        credential: AppCredentialJson,
    ) = appCredentialCache.save(domain, credential)

    override suspend fun getCurrentAuthorizedUser() = tokenCache.getCurrent()

    override suspend fun switchCurrentAuthorizedUser(id: AccountId) = tokenCache.setCurrent(id)

    override suspend fun saveAuthorizedUser(
        id: AccountId,
        domain: Domain,
        accessToken: String,
    ): AuthorizedUser {
        tokenCache.add(id, domain, accessToken)
        return tokenCache.setCurrent(id)
    }

    override suspend fun expireCurrentAuthorizedUser() {
        getCurrentAuthorizedUser()?.let { tokenCache.delete(it.id) }
        tokenCache.getAll().firstOrNull()?.let {
            switchCurrentAuthorizedUser(it.id)
        }
    }
}
