package noctiluca.network.mastodon.json.previewcard

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 *  ref. https://docs.joinmastodon.org/entities/PreviewCard/
 */

@Serializable
data class PreviewCardJson(
    val url: String,
    val title: String,
    val description: String,
    val type: String,
    @SerialName("author_name")
    val authorName: String,
    @SerialName("author_url")
    val authorUrl: String,
    @SerialName("provider_name")
    val providerName: String,
    @SerialName("provide_url")
    val providerUrl: String?,
    val html: String,
    val width: Int,
    val height: Int,
    val image: String?,
    @SerialName("embed_url")
    val embedUrl: String,
    val blurhash: String?,
)
