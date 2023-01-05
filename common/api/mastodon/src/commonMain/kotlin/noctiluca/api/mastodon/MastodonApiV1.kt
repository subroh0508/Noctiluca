package noctiluca.api.mastodon

import noctiluca.api.mastodon.json.account.AccountCredentialJson
import noctiluca.api.mastodon.json.instance.V1InstanceJson

interface MastodonApiV1 {
    suspend fun getInstance(
        hostname: String,
    ): V1InstanceJson

    suspend fun getVerifyAccountsCredentials(
        hostname: String,
    ): AccountCredentialJson

    suspend fun getTimelinesPublic(

    ): List<Unit>
}
