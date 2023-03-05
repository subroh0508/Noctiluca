package noctiluca.authentication.infra.repository.impl

import noctiluca.api.authentication.AuthenticationApi
import noctiluca.api.authentication.json.AppCredentialJson
import noctiluca.api.token.LocalTokenCache
import noctiluca.authentication.infra.repository.TokenRepository
import noctiluca.authentication.infra.repository.local.AppCredentialCache
import noctiluca.authentication.model.AppCredential
import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser
import noctiluca.model.Domain
import noctiluca.model.Uri

internal class TokenRepositoryImpl(
    private val api: AuthenticationApi,
    private val appCredentialCache: AppCredentialCache,
    private val tokenCache: LocalTokenCache,
) : TokenRepository {
    override suspend fun fetchAppCredential(
        domain: Domain,
        client: String,
        redirectUri: Uri,
    ): AppCredential {
        val (json, authorizeUrl) = api.postApps(domain.value, client, redirectUri.value)

        return AppCredential(json.clientId, json.clientSecret, domain, Uri(authorizeUrl))
    }

    override suspend fun cacheAppCredential(appCredential: AppCredential) {
        appCredentialCache.save(appCredential.domain, AppCredentialJson(appCredential.clientId, appCredential.clientSecret))
    }

    override suspend fun fetchToken(
        code: String,
        redirectUri: Uri,
    ): AuthorizedUser? {
        val (domain, clientId, clientSecret) = appCredentialCache.getCurrent()?.let { (host, json) ->
            Triple(host, json.clientId, json.clientSecret)
        } ?: return null

        val accessToken = api.postOAuthToken(
            domain.value,
            clientId,
            clientSecret,
            redirectUri.value,
            code,
        ).accessToken

        val id = AccountId(api.getVerifyAccountsCredentials(domain.value, accessToken).id)

        tokenCache.add(id, domain, accessToken)

        return tokenCache.setCurrent(id)
    }

    override suspend fun getCurrent() = tokenCache.getCurrent()

    override suspend fun switch(id: AccountId) = tokenCache.setCurrent(id)

    override suspend fun expireCurrent() {
        getCurrent()?.let { tokenCache.delete(it.id) }
        tokenCache.getAll().firstOrNull()?.let { switch(it.id) }
    }
}
