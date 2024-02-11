package noctiluca.data.authentication.impl

import noctiluca.data.authentication.AuthenticationRepository
import noctiluca.datastore.AppCredentialDataStore
import noctiluca.datastore.AuthenticationTokenDataStore
import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser
import noctiluca.model.Domain
import noctiluca.model.Uri
import noctiluca.model.authentication.AppCredential
import noctiluca.network.authentication.AuthenticationApi

internal class AuthenticationRepositoryImpl(
    private val appCredentialDataStore: AppCredentialDataStore,
    private val authenticationTokenDataStore: AuthenticationTokenDataStore,
    private val api: AuthenticationApi,
) : AuthenticationRepository {
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

        return authenticationTokenDataStore.add(id, domain, accessToken).find { it.id == id }
    }

    override suspend fun switchAccessToken(id: AccountId) = authenticationTokenDataStore.setCurrent(id)
}
