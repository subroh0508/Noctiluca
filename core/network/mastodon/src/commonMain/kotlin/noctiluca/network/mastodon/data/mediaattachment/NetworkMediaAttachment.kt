package noctiluca.network.mastodon.data.mediaattachment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import noctiluca.network.mastodon.serializer.MediaAttachmentSerializer

/**
 *  ref. https://docs.joinmastodon.org/entities/MediaAttachment/
 */
@Serializable(with = MediaAttachmentSerializer::class)
data class NetworkMediaAttachment(
    val id: String,
    val type: String,
    val url: String,
    @SerialName("preview_url")
    val previewUrl: String,
    @SerialName("remote_url")
    val remoteUrl: String?,
    val meta: NetworkMediaAttachmentMeta,
    val description: String?,
    val blurhash: String?,
)
