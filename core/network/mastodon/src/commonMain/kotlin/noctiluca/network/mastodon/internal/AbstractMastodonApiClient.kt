package noctiluca.network.mastodon.internal

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import noctiluca.model.AuthorizedTokenNotFoundException
import noctiluca.model.HttpException
import noctiluca.model.HttpUnauthorizedException
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
    ) = handleResponseException {
        get(resource, builder = {
            setAccessTokenAndHost(domain, accessToken, skipAuthorization)
            httpRequestBuilder()
        })
    }

    protected suspend inline fun <reified T : Any> HttpClient.post(
        resource: T,
        domain: String? = null,
        skipAuthorization: Boolean = false,
    ) = handleResponseException {
        post(resource) {
            setAccessTokenAndHost(domain, skipAuthorization = skipAuthorization)
        }
    }

    protected suspend inline fun <reified T : Any, reified E : Any> HttpClient.post(
        resource: T,
        body: E,
        domain: String? = null,
        skipAuthorization: Boolean = false,
    ) = handleResponseException {
        post(resource) {
            setAccessTokenAndHost(domain, skipAuthorization = skipAuthorization)
            setBody(body)
        }
    }

    protected suspend fun HttpRequestBuilder.setAccessTokenAndHost(
        domain: String? = null,
        accessToken: String? = null,
        skipAuthorization: Boolean = false,
    ) {
        host = domain ?: getCurrentDomain()

        if (!skipAuthorization) {
            bearerAuth(accessToken ?: getCurrentAccessToken())
        }
    }

    protected inline fun handleResponseException(
        block: () -> HttpResponse,
    ) = runCatching { block() }.getOrElse { e ->
        if (e is ResponseException) {
            when (val status = e.response.status) {
                HttpStatusCode.Unauthorized -> throw HttpUnauthorizedException(status.value, e)
                else -> throw HttpException(status.value, e)
            }
        }

        throw e
    }

    private suspend fun getCurrentAccessToken() =
        token.getCurrentAccessToken() ?: throw AuthorizedTokenNotFoundException

    private suspend fun getCurrentDomain() = token.getCurrentDomain() ?: throw AuthorizedTokenNotFoundException
}
