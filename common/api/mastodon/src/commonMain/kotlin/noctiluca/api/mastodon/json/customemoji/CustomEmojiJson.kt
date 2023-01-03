package noctiluca.api.mastodon.json.customemoji

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 *  ref. https://docs.joinmastodon.org/entities/CustomEmoji/
 */
@Serializable
data class CustomEmojiJson(
    val shortcode: String,
    val url: String,
    @SerialName("static_url")
    val staticUrl: String,
    @SerialName("visible_in_picker")
    val visibleInPicker: Boolean,
    val category: String,
)
