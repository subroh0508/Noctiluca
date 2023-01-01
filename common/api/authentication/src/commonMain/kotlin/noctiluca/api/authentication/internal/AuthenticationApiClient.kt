package noctiluca.api.authentication.internal

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import noctiluca.api.authentication.AuthenticationApi
import noctiluca.api.authentication.json.AppCredentialJson
import noctiluca.api.authentication.json.TokenJson
import noctiluca.api.authentication.params.GetAccountsVerifyCredential
import noctiluca.api.authentication.params.PostApps
import noctiluca.api.authentication.params.PostApps.Companion.ESCAPED_SCOPE
import noctiluca.api.authentication.params.PostOauthToken

internal class AuthenticationApiClient(
    private val client: HttpClient,
) : AuthenticationApi {
    companion object {
        private const val ENDPOINT_POST_APPS = "/api/v1/apps"
        private const val GET_OAUTH_AUTHORIZE = "/oauth/authorize"
        private const val ENDPOINT_POST_OAUTH_TOKEN = "/oauth/token"
        private const val ENDPOINT_GET_ACCOUNTS_VERIFY_CREDENTIALS = "/api/v1/accounts/verify_credentials"

        private const val RESPONSE_TYPE = "response_type"
        private const val CLIENT_ID = "client_id"
        private const val CLIENT_SECRET = "client_secret"
        private const val REDIRECT_URI = "redirect_uri"
        private const val SCOPE = "scope"
    }

    override suspend fun postApps(
        hostname: String,
        clientName: String,
        redirectUri: String
    ) = client.post(ENDPOINT_POST_APPS) {
        host = hostname
        setBody(PostApps.Request(clientName, redirectUri))
    }.body<AppCredentialJson>().let { credential ->
        credential to buildAuthorizeUrl(hostname, credential, redirectUri)
    }

    override suspend fun postOAuthToken(
        hostname: String,
        clientId: String,
        clientSecret: String,
        redirectUri: String,
        code: String
    ): TokenJson {
        return client.post(ENDPOINT_POST_OAUTH_TOKEN) {
            host = hostname
            setBody(PostOauthToken.Request(clientId, clientSecret, redirectUri, code))
        }.body()
    }

    override suspend fun getVerifyAccountsCredentials(
        hostname: String,
        accessToken: String,
    ): GetAccountsVerifyCredential.Response = client.get(ENDPOINT_GET_ACCOUNTS_VERIFY_CREDENTIALS) {
        host = hostname
        bearerAuth(accessToken)
    }.body()

    private fun buildAuthorizeUrl(
        hostname: String,
        credential: AppCredentialJson,
        redirectUri: String,
    ) = buildString {
        append("${URLProtocol.HTTPS.name}://$hostname$GET_OAUTH_AUTHORIZE?")
        val query = mapOf(
            RESPONSE_TYPE to "code",
            CLIENT_ID to credential.clientId,
            //CLIENT_SECRET to credential.clientSecret,
            REDIRECT_URI to redirectUri,
            SCOPE to ESCAPED_SCOPE,
        ).map { (k, v) -> "$k=$v" }.joinToString("&")

        append(query)
    }
}