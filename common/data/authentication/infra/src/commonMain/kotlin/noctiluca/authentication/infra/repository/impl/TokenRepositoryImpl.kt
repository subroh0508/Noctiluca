package noctiluca.authentication.infra.repository.impl

import noctiluca.api.authentication.AuthenticationApi
import noctiluca.api.authentication.json.AppCredentialJson
import noctiluca.authentication.infra.repository.TokenRepository
import noctiluca.authentication.infra.repository.local.LocalTokenRepository
import noctiluca.authentication.model.AppCredential
import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser
import noctiluca.model.Domain
import noctiluca.model.Uri

internal class TokenRepositoryImpl(
    private val local: LocalTokenRepository,
    private val api: AuthenticationApi,
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
        local.saveAppCredential(
            appCredential.domain,
            AppCredentialJson(appCredential.clientId, appCredential.clientSecret)
        )
    }

    override suspend fun fetchToken(
        code: String,
        redirectUri: Uri,
    ): AuthorizedUser? {
        val (domain, json) = local.getCurrentAppCredential() ?: return null

        val accessToken = api.postOAuthToken(
            domain.value,
            json.clientId,
            json.clientSecret,
            redirectUri.value,
            code,
        ).accessToken

        val id = AccountId(api.getVerifyAccountsCredentials(domain.value, accessToken).id)

        return local.saveAuthorizedUser(id, domain, accessToken)
    }

    override suspend fun getCurrent() = local.getCurrentAuthorizedUser()

    override suspend fun switch(id: AccountId) = local.switchCurrentAuthorizedUser(id)

    override suspend fun expireCurrent() = local.expireAuthorizedUser()
}
