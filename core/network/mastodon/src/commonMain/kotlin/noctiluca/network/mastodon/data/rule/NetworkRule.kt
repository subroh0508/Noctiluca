package noctiluca.network.mastodon.data.rule

import kotlinx.serialization.Serializable

/**
 *  ref. https://docs.joinmastodon.org/entities/Rule/
 */

@Serializable
data class NetworkRule(
    val id: String,
    val text: String,
)
