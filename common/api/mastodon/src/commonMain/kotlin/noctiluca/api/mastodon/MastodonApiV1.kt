package noctiluca.api.mastodon

import noctiluca.api.mastodon.json.account.AccountCredentialJson
import noctiluca.api.mastodon.json.instance.V1InstanceJson
import noctiluca.api.mastodon.json.status.StatusJson

interface MastodonApiV1 {
    suspend fun getInstance(
        domain: String,
    ): V1InstanceJson

    suspend fun getVerifyAccountsCredentials(
        domain: String,
    ): AccountCredentialJson

    suspend fun getTimelinesPublic(
        local: Boolean = false,
        remote: Boolean = false,
        onlyMedia: Boolean = false,
        maxId: String? = null,
        sinceId: String? = null,
        minId: String? = null,
        limit: Int = 20,
    ): List<StatusJson>
}
