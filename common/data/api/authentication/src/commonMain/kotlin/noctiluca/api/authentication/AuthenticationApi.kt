package noctiluca.api.authentication

import noctiluca.api.authentication.json.AppCredential
import noctiluca.api.authentication.json.Token
import noctiluca.api.authentication.params.GetAccountsVerifyCredential

interface AuthenticationApi {
    suspend fun postApps(
        hostname: String,
        clientName: String,
        redirectUri: String,
    ): Pair<AppCredential, String>

    suspend fun postOAuthToken(
        hostname: String,
        clientId: String,
        clientSecret: String,
        redirectUri: String,
        code: String,
    ): Token

    suspend fun getVerifyAccountsCredentials(
        hostname: String,
        accessToken: String,
    ): GetAccountsVerifyCredential.Response
}