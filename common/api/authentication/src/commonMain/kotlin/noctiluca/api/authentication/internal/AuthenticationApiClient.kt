package noctiluca.api.authentication.internal

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.resources.*
import noctiluca.api.authentication.Api
import noctiluca.api.authentication.AuthenticationApi
import noctiluca.api.authentication.OAuth
import noctiluca.api.authentication.json.AppCredentialJson
import noctiluca.api.authentication.json.TokenJson
import noctiluca.api.authentication.params.GetAccountsVerifyCredential
import noctiluca.api.authentication.params.PostApps
import noctiluca.api.authentication.params.PostOauthToken

internal class AuthenticationApiClient(
    private val client: HttpClient,
) : AuthenticationApi {
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
    ): Pair<AppCredentialJson, String> {
        val request = PostApps.Request(clientName, redirectUri)

        return client.post(Api.V1.Apps()) {
            host = domain
            setBody(request)
        }.body<AppCredentialJson>().let { credential ->
            credential to buildAuthorizeUrl(domain, credential, redirectUri, request.scopes)
        }
    }

    override suspend fun postOAuthToken(
        domain: String,
        clientId: String,
        clientSecret: String,
        redirectUri: String,
        code: String
    ): TokenJson = client.post(OAuth.Token()) {
        host = domain
        setBody(PostOauthToken.Request(clientId, clientSecret, redirectUri, code))
    }.body()

    override suspend fun getVerifyAccountsCredentials(
        domain: String,
        accessToken: String,
    ): GetAccountsVerifyCredential.Response = client.get(Api.V1.Accounts.VerifyCredentials()) {
        host = domain
        bearerAuth(accessToken)
    }.body()

    private fun buildAuthorizeUrl(
        domain: String,
        credential: AppCredentialJson,
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
}
