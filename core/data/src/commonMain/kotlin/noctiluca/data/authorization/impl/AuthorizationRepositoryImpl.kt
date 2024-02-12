package noctiluca.data.authorization.impl

import noctiluca.data.authorization.AuthorizationRepository
import noctiluca.datastore.AppCredentialDataStore
import noctiluca.datastore.AuthorizationTokenDataStore
import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser
import noctiluca.model.Domain
import noctiluca.model.Uri
import noctiluca.model.authorization.AppCredential
import noctiluca.network.authorization.AuthorizationApi

internal class AuthorizationRepositoryImpl(
    private val appCredentialDataStore: AppCredentialDataStore,
    private val authorizationTokenDataStore: AuthorizationTokenDataStore,
    private val api: AuthorizationApi,
) : AuthorizationRepository {
    override suspend fun fetchAuthorizeUrl(
        domain: Domain,
        client: String,
        redirectUri: Uri,
    ): Uri {
        val (json, authorizeUrl) = api.postApps(domain.value, client, redirectUri.value)

        val credential = AppCredential(json.clientId, json.clientSecret, domain, Uri(authorizeUrl))
        appCredentialDataStore.save(credential)

        return credential.authorizeUrl
    }

    override suspend fun fetchAccessToken(
        code: String,
        redirectUri: Uri,
    ): AuthorizedUser? {
        val (clientId, clientSecret, domain) = appCredentialDataStore.getCurrent() ?: return null

        val accessToken = api.postOAuthToken(
            domain.value,
            clientId,
            clientSecret,
            redirectUri.value,
            code,
        ).accessToken

        val id = AccountId(api.getVerifyAccountsCredentials(domain.value, accessToken).id)

        return authorizationTokenDataStore.add(id, domain, accessToken).find { it.id == id }
    }
}
