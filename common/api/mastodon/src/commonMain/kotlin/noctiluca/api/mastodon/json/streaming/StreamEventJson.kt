package noctiluca.api.mastodon.json.streaming

import kotlinx.serialization.Serializable

@Serializable
data class StreamEventJson(
    val stream: List<String>,
    val event: String,
    val payload: String?,
)
