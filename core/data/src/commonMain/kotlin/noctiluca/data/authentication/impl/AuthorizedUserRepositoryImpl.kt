package noctiluca.data.authentication.impl

import noctiluca.data.authentication.AuthorizedUserRepository
import noctiluca.datastore.AppCredentialDataStore
import noctiluca.datastore.AuthenticationTokenDataStore
import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser
import noctiluca.model.Uri
import noctiluca.network.authentication.AuthenticationApi

internal class AuthorizedUserRepositoryImpl(
    private val appCredentialDataStore: AppCredentialDataStore,
    private val authenticationTokenDataStore: AuthenticationTokenDataStore,
    private val api: AuthenticationApi,
) : AuthorizedUserRepository {
    override suspend fun fetch(
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

    override suspend fun switch(id: AccountId) = authenticationTokenDataStore.setCurrent(id)

    override suspend fun expireCurrent() {
        authenticationTokenDataStore.getCurrent()?.let { authenticationTokenDataStore.delete(it.id) }
        authenticationTokenDataStore.getAll().firstOrNull()?.let {
            authenticationTokenDataStore.setCurrent(it.id)
        }
    }
}
