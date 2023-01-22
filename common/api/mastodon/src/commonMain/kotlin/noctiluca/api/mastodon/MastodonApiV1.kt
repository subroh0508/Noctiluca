package noctiluca.api.mastodon

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json
import noctiluca.api.mastodon.json.account.AccountCredentialJson
import noctiluca.api.mastodon.json.instance.V1InstanceJson
import noctiluca.api.mastodon.json.status.StatusJson
import noctiluca.api.mastodon.json.streaming.StreamEventJson

interface MastodonApiV1 {
    val json: Json

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

    suspend fun getTimelinesHome(
        maxId: String? = null,
        sinceId: String? = null,
        minId: String? = null,
        limit: Int = 20,
    ): List<StatusJson>

    suspend fun streaming(
        stream: String,
        type: String,
        listId: String? = null,
        tag: String? = null,
    ): Flow<StreamEventJson>
}
