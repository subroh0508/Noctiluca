package noctiluca.network.authorization.internal

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.resources.*
import noctiluca.model.HttpException
import noctiluca.network.authorization.Api
import noctiluca.network.authorization.AuthorizationApi
import noctiluca.network.authorization.OAuth
import noctiluca.network.authorization.data.NetworkAppCredential
import noctiluca.network.authorization.data.NetworkToken
import noctiluca.network.authorization.params.GetAccountsVerifyCredential
import noctiluca.network.authorization.params.PostApps
import noctiluca.network.authorization.params.PostOauthToken

internal class AuthorizationApiClient(
    private val client: HttpClient,
) : AuthorizationApi {
    companion object {
        private const val RESPONSE_TYPE = "response_type"
        private const val CLIENT_ID = "client_id"
        private const val REDIRECT_URI = "redirect_uri"
        private const val SCOPE = "scope"

        private const val REQUIRED_RESPONSE_TYPE = "code"
    }

    override suspend fun postApps(
        domain: String,
        clientName: String,
        redirectUri: String
    ): Pair<NetworkAppCredential, String> {
        val request = PostApps.Request(clientName, redirectUri)

        return handleResponseException {
            client.post(Api.V1.Apps()) {
                host = domain
                setBody(request)
            }
        }.body<NetworkAppCredential>().let { credential ->
            credential to buildAuthorizeUrl(domain, credential, redirectUri, request.scopes)
        }
    }

    override suspend fun postOAuthToken(
        domain: String,
        clientId: String,
        clientSecret: String,
        redirectUri: String,
        code: String
    ): NetworkToken = handleResponseException {
        client.post(OAuth.Token()) {
            host = domain
            setBody(PostOauthToken.Request(clientId, clientSecret, redirectUri, code))
        }
    }.body()

    override suspend fun getVerifyAccountsCredentials(
        domain: String,
        accessToken: String,
    ): GetAccountsVerifyCredential.Response = handleResponseException {
        client.get(Api.V1.Accounts.VerifyCredentials()) {
            host = domain
            bearerAuth(accessToken)
        }
    }.body()

    private fun buildAuthorizeUrl(
        domain: String,
        credential: NetworkAppCredential,
        redirectUri: String,
        scopes: String,
    ): String = URLBuilder().also { url ->
        href(
            client.plugin(Resources).resourcesFormat,
            OAuth.Authorize(),
            url.apply {
                protocol = URLProtocol.HTTPS
                host = domain
                parameters.apply {
                    append(RESPONSE_TYPE, REQUIRED_RESPONSE_TYPE)
                    append(CLIENT_ID, credential.clientId)
                    append(REDIRECT_URI, redirectUri)
                    append(SCOPE, scopes)
                }
            },
        )
    }.build().toString()

    private inline fun handleResponseException(
        block: () -> HttpResponse,
    ) = runCatching { block() }.getOrElse { e ->
        if (e is ResponseException) {
            throw HttpException(e.response.status.value, e)
        }

        throw e
    }
}
