package noctiluca.api.mastodon

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json
import noctiluca.api.mastodon.json.account.AccountCredentialJson
import noctiluca.api.mastodon.json.instance.V1InstanceJson
import noctiluca.api.mastodon.json.status.StatusJson
import noctiluca.api.mastodon.json.streaming.StreamEventJson

interface MastodonStream {
    val json: Json

    suspend fun streaming(
        stream: String,
        type: String,
        listId: String? = null,
        tag: String? = null,
    ): Flow<StreamEventJson>
}
