package noctiluca.data.authentication.impl

import noctiluca.data.authentication.AppCredentialRepository
import noctiluca.model.Domain
import noctiluca.model.Uri
import noctiluca.model.authentication.AppCredential
import noctiluca.datastore.AppCredentialDataStore
import noctiluca.network.authentication.AuthenticationApi

internal class AppCredentialRepositoryImpl(
    private val dataStore: AppCredentialDataStore,
    private val api: AuthenticationApi,
) : AppCredentialRepository {
    override suspend fun fetchAppCredential(
        domain: Domain,
        client: String,
        redirectUri: Uri,
    ): AppCredential {
        val (json, authorizeUrl) = api.postApps(domain.value, client, redirectUri.value)

        val credential = AppCredential(json.clientId, json.clientSecret, domain, Uri(authorizeUrl))
        dataStore.save(credential)

        return credential
    }
}
