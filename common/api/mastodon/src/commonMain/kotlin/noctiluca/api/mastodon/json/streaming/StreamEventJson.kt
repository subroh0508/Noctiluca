package noctiluca.api.mastodon.json.streaming

import kotlinx.serialization.Serializable
import noctiluca.api.mastodon.json.status.StatusJson

/**
 *  ref. https://docs.joinmastodon.org/methods/streaming/#events
 */

@Serializable
data class StreamEventJson(
    val stream: List<String>,
    val event: String,
    val payload: Payload?,
) {
    @Serializable
    sealed class Payload {
        data class Updated(val status: StatusJson) : Payload()
        data class Deleted(val id: String) : Payload()
        data class StatusEdited(val status: StatusJson) : Payload()
    }
}
