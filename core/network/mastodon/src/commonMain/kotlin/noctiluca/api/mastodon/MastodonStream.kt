package noctiluca.api.mastodon

import kotlinx.coroutines.flow.Flow
import noctiluca.api.mastodon.json.streaming.StreamEventJson

interface MastodonStream {
    suspend fun streaming(
        stream: String,
        type: String,
        listId: String? = null,
        tag: String? = null,
    ): Flow<StreamEventJson>
}
