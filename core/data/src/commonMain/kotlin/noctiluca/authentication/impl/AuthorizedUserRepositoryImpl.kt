package noctiluca.authentication.impl

import noctiluca.authentication.AuthorizedUserRepository
import noctiluca.datastore.AppCredentialDataStore
import noctiluca.datastore.TokenDataStore
import noctiluca.model.AccountId
import noctiluca.model.Uri
import noctiluca.network.authentication.AuthenticationApi

internal class AuthorizedUserRepositoryImpl(
    private val appCredentialDataStore: AppCredentialDataStore,
    private val tokenDataStore: TokenDataStore,
    private val api: AuthenticationApi,
) : AuthorizedUserRepository {
    override suspend fun fetchAccessToken(
        code: String,
        redirectUri: Uri,
    ): String? {
        val (clientId, clientSecret, domain) = appCredentialDataStore.getCurrent() ?: return null

        val accessToken = api.postOAuthToken(
            domain.value,
            clientId,
            clientSecret,
            redirectUri.value,
            code,
        ).accessToken

        val id = AccountId(api.getVerifyAccountsCredentials(domain.value, accessToken).id)
        tokenDataStore.add(id, domain, accessToken)

        return accessToken
    }

    override suspend fun getCurrent() = tokenDataStore.getCurrent()

    override suspend fun switch(id: AccountId) = tokenDataStore.setCurrent(id)

    override suspend fun expireCurrent() {
        getCurrent()?.let { tokenDataStore.delete(it.id) }
        tokenDataStore.getAll().firstOrNull()?.let {
            switch(it.id)
        }
    }
}
