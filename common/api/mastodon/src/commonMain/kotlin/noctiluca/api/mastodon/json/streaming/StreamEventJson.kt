package noctiluca.api.mastodon.json.streaming

import kotlinx.serialization.Serializable

/**
 *  ref. https://docs.joinmastodon.org/methods/streaming/#events
 */

@Serializable
data class StreamEventJson(
    val stream: List<String>,
    val event: String,
    val payload: String?,
)
