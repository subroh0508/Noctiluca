package noctiluca.network.authorization

import noctiluca.network.authorization.data.NetworkAppCredential
import noctiluca.network.authorization.data.NetworkToken
import noctiluca.network.authorization.params.GetAccountsVerifyCredential

interface AuthorizationApi {
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
