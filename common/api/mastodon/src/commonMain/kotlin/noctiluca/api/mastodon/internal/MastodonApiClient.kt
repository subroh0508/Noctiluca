package noctiluca.api.mastodon.internal

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import noctiluca.api.mastodon.MastodonApi
import noctiluca.api.mastodon.params.GetInstance
import noctiluca.api.token.Token

internal class MastodonApiClient(
    private val token: Token.Cache,
    private val client: HttpClient,
) : MastodonApi {
    override suspend fun getInstance(
        hostname: String,
    ) = client.get<GetInstance.Response>(
        GetInstance.ENDPOINT,
        hostname = hostname,
        skipAuthorization = true,
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