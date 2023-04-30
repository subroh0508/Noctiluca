package noctiluca.api.mastodon.json.mediaattachment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 *  ref. https://docs.joinmastodon.org/entities/MediaAttachment/
 */
@Serializable
data class MediaAttachmentJson(
    val id: String,
    val type: String,
    val url: String,
    @SerialName("preview_url")
    val previewUrl: String,
    @SerialName("remote_url")
    val remoteUrl: String?,
    // val meta: Meta,
    val description: String?,
    val blurhash: String?,
)
