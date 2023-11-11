package noctiluca.network.mastodon.internal

import io.ktor.client.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import noctiluca.network.mastodon.AuthenticationTokenProvider

abstract class AbstractMastodonApiClient(
    private val token: AuthenticationTokenProvider,
) {
    protected suspend inline fun <reified T : Any> HttpClient.get(
        resource: T,
        domain: String? = null,
        accessToken: String? = null,
        skipAuthorization: Boolean = false,
        httpRequestBuilder: HttpRequestBuilder.() -> Unit = {},
    ) = get(resource, builder = {
        setAccessTokenAndHost(domain, accessToken, skipAuthorization)
        httpRequestBuilder()
    })

    protected suspend inline fun <reified T : Any> HttpClient.post(
        resource: T,
        domain: String? = null,
        skipAuthorization: Boolean = false,
    ) = post(resource) {
        setAccessTokenAndHost(domain, skipAuthorization = skipAuthorization)
    }

    protected suspend inline fun <reified T : Any, reified E : Any> HttpClient.post(
        resource: T,
        body: E,
        domain: String? = null,
        skipAuthorization: Boolean = false,
    ) = post(resource) {
        setAccessTokenAndHost(domain, skipAuthorization = skipAuthorization)
        setBody(body)
    }

    protected suspend fun HttpRequestBuilder.setAccessTokenAndHost(
        domain: String? = null,
        accessToken: String? = null,
        skipAuthorization: Boolean = false,
    ) {
        val token = accessToken ?: getCurrentAccessToken()
        val host = domain ?: getCurrentDomain()

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
