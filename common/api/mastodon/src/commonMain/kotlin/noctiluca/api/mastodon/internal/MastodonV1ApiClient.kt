package noctiluca.api.mastodon.internal

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import noctiluca.api.mastodon.MastodonV1Api
import noctiluca.api.mastodon.json.account.AccountCredentialJson
import noctiluca.api.mastodon.json.instance.V1InstanceJson
import noctiluca.repository.TokenCache

internal class MastodonV1ApiClient(
    private val token: TokenCache,
    private val client: HttpClient,
) : MastodonV1Api {
    override suspend fun getInstance(
        hostname: String,
    ) = client.get<V1InstanceJson>(
        Api.V1.Instance,
        hostname = hostname,
        skipAuthorization = true,
    )

    override suspend fun getVerifyAccountsCredentials(
        hostname: String,
    ) = client.get<AccountCredentialJson>(
        Api.V1.Accounts.VerifyCredentials,
        hostname = hostname,
    )

    private suspend inline fun <reified T: Any> HttpClient.get(
        endpoint: String,
        hostname: String? = null,
        skipAuthorization: Boolean = false,
    ): T = get(endpoint) {
        setAccessTokenAndHost(hostname, skipAuthorization)
    }.body()

    private suspend inline fun <reified T: Any, reified E: Any> HttpClient.post(
        endpoint: String,
        body: E,
        hostname: String? = null,
        skipAuthorization: Boolean = false,
    ): T = post(endpoint) {
        setAccessTokenAndHost(hostname, skipAuthorization)
        setBody(body)
    }.body()

    private suspend fun HttpRequestBuilder.setAccessTokenAndHost(
        hostname: String?,
        skipAuthorization: Boolean,
    ) {
        val token = getCurrentAccessToken()
        val host = hostname ?: getCurrentDomain()

        if (host != null) {
            this.host = host
        }

        if (token != null && !skipAuthorization) {
            bearerAuth(token)
        }
    }


    private suspend fun getCurrentAccessToken() = token.getCurrentAccessToken()
    private suspend fun getCurrentDomain() = token.getCurrentDomain()
}