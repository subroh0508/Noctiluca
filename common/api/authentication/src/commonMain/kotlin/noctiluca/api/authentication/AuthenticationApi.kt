package noctiluca.api.authentication

import noctiluca.api.authentication.json.AppCredentialJson
import noctiluca.api.authentication.json.TokenJson
import noctiluca.api.authentication.params.GetAccountsVerifyCredential

interface AuthenticationApi {
    suspend fun postApps(
        domain: String,
        clientName: String,
        redirectUri: String,
    ): Pair<AppCredentialJson, String>

    suspend fun postOAuthToken(
        domain: String,
        clientId: String,
        clientSecret: String,
        redirectUri: String,
        code: String,
    ): TokenJson

    suspend fun getVerifyAccountsCredentials(
        domain: String,
        accessToken: String,
    ): GetAccountsVerifyCredential.Response
}