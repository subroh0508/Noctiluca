package noctiluca.api.mastodon

import noctiluca.api.mastodon.json.account.AccountCredentialJson
import noctiluca.api.mastodon.params.GetInstance

interface MastodonApi {
    suspend fun getInstance(
        hostname: String,
    ): GetInstance.Response

    suspend fun getVerifyAccountsCredentials(
        hostname: String,
    ): AccountCredentialJson
}