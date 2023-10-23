package noctiluca.network.mastodon.data.streaming

import kotlinx.serialization.Serializable
import noctiluca.network.mastodon.data.status.NetworkStatus

/**
 *  ref. https://docs.joinmastodon.org/methods/streaming/#events
 */

@Serializable
data class NetworkStreamEvent(
    val stream: List<String>,
    val event: String,
    val payload: Payload?,
) {
    @Serializable
    sealed class Payload {
        data class Updated(val status: NetworkStatus) : Payload()
        data class Deleted(val id: String) : Payload()
        data class StatusEdited(val status: NetworkStatus) : Payload()
    }
}
