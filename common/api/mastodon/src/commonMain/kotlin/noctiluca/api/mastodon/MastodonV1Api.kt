package noctiluca.api.mastodon

import noctiluca.api.mastodon.json.account.AccountCredentialJson
import noctiluca.api.mastodon.json.instance.V1InstanceJson

interface MastodonV1Api {
    suspend fun getInstance(
        hostname: String,
    ): V1InstanceJson

    suspend fun getVerifyAccountsCredentials(
        hostname: String,
    ): AccountCredentialJson
}