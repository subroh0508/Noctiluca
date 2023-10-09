package noctiluca.network.mastodon

import kotlinx.coroutines.flow.Flow
import noctiluca.network.mastodon.json.streaming.StreamEventJson

interface MastodonStream {
    suspend fun streaming(
        stream: String,
        type: String,
        listId: String? = null,
        tag: String? = null,
    ): Flow<StreamEventJson>
}
