package noctiluca.api.authentication

import noctiluca.api.authentication.json.AppCredentialJson
import noctiluca.api.authentication.json.TokenJson
import noctiluca.api.authentication.params.GetAccountsVerifyCredential

interface AuthenticationApi {
    suspend fun postApps(
        hostname: String,
        clientName: String,
        redirectUri: String,
    ): Pair<AppCredentialJson, String>

    suspend fun postOAuthToken(
        hostname: String,
        clientId: String,
        clientSecret: String,
        redirectUri: String,
        code: String,
    ): TokenJson

    suspend fun getVerifyAccountsCredentials(
        hostname: String,
        accessToken: String,
    ): GetAccountsVerifyCredential.Response
}