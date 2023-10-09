package noctiluca.network.authentication

import noctiluca.network.authentication.json.NetworkAppCredential
import noctiluca.network.authentication.json.NetworkToken
import noctiluca.network.authentication.params.GetAccountsVerifyCredential

interface AuthenticationApi {
    suspend fun postApps(
        domain: String,
        clientName: String,
        redirectUri: String,
    ): Pair<NetworkAppCredential, String>

    suspend fun postOAuthToken(
        domain: String,
        clientId: String,
        clientSecret: String,
        redirectUri: String,
        code: String,
    ): NetworkToken

    suspend fun getVerifyAccountsCredentials(
        domain: String,
        accessToken: String,
    ): GetAccountsVerifyCredential.Response
}
