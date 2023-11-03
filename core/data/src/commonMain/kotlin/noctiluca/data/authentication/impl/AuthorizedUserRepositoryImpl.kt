package noctiluca.data.authentication.impl

import noctiluca.data.authentication.AuthorizedUserRepository
import noctiluca.datastore.AppCredentialDataStore
import noctiluca.datastore.TokenDataStore
import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser
import noctiluca.model.Uri
import noctiluca.network.authentication.AuthenticationApi

internal class AuthorizedUserRepositoryImpl(
    private val appCredentialDataStore: AppCredentialDataStore,
    private val tokenDataStore: TokenDataStore,
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

        return tokenDataStore.add(id, domain, accessToken).find { it.id == id }
    }

    override suspend fun switch(id: AccountId) = tokenDataStore.setCurrent(id)

    override suspend fun expireCurrent() {
        tokenDataStore.getCurrent()?.let { tokenDataStore.delete(it.id) }
        tokenDataStore.getAll().firstOrNull()?.let {
            tokenDataStore.setCurrent(it.id)
        }
    }
}
