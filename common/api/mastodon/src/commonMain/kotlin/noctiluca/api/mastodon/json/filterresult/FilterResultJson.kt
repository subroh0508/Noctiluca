package noctiluca.api.mastodon.json.filterresult

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 *  ref. https://docs.joinmastodon.org/entities/FilterResult/
 */

@Serializable
data class FilterResultJson(
    val filter: Filter,
    @SerialName("keyword_matches")
    val keywordMatches: List<String>,
    @SerialName("status_matches")
    val statusMatches: List<String>,
) {
    @Serializable
    data class Filter(
        val id: String,
        val title: String,
        val context: String,
    )
}
