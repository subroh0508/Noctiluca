package noctiluca.network.mastodon.data.status

import kotlinx.serialization.Serializable

@Serializable
data class NetworkStatusesContext(
    val ancestors: List<NetworkStatus>,
    val descendants: List<NetworkStatus>,
)
