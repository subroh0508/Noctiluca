package noctiluca.network.mastodon.json.rule

import kotlinx.serialization.Serializable

/**
 *  ref. https://docs.joinmastodon.org/entities/Rule/
 */

@Serializable
data class RuleJson(
    val id: String,
    val text: String,
)
