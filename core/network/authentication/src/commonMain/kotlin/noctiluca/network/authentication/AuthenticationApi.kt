package noctiluca.network.authentication

import noctiluca.network.authentication.data.NetworkAppCredential
import noctiluca.network.authentication.data.NetworkToken
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
