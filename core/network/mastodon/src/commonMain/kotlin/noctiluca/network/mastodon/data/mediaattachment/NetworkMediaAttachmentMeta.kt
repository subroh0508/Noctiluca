package noctiluca.network.mastodon.data.mediaattachment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class NetworkMediaAttachmentMeta {
    @Serializable
    data class Image(
        val original: ImageOriginal,
        val small: Small,
        val focus: Focus,
    ) : NetworkMediaAttachmentMeta()

    @Serializable
    data class Video(
        val length: String,
        val duration: Double,
        val fps: Int,
        val size: String,
        val width: Int,
        val height: Int,
        val aspect: Double,
        @SerialName("audio_encode")
        val audioEncode: String,
        @SerialName("audio_bitrate")
        val audioBitrate: String,
        @SerialName("audio_channels")
        val audioChannels: String,
        val original: VideoOriginal,
        val small: Small,
    ) : NetworkMediaAttachmentMeta()

    @Serializable
    data class GifV(
        val length: String,
        val duration: Double,
        val fps: Int,
        val size: String,
        val width: Int,
        val height: Int,
        val aspect: Double,
        val original: VideoOriginal,
        val small: Small,
    ) : NetworkMediaAttachmentMeta()

    @Serializable
    data class Audio(
        val length: String,
        val duration: Double,
        @SerialName("audio_encode")
        val audioEncode: String,
        @SerialName("audio_bitrate")
        val audioBitrate: String,
        @SerialName("audio_channels")
        val audioChannels: String,
        val original: AudioOriginal,
    ) : NetworkMediaAttachmentMeta()

    @Serializable
    data class ImageOriginal(
        val width: Int,
        val height: Int,
        val size: String,
        val aspect: Double,
    )

    @Serializable
    data class VideoOriginal(
        val width: Int,
        val height: Int,
        @SerialName("frame_rate")
        val frameRate: String,
        val duration: Double,
        val bitrate: Int,
    )

    @Serializable
    data class AudioOriginal(
        val duration: Double,
        val bitrate: Int,
    )

    @Serializable
    data class Small(
        val width: Int,
        val height: Int,
        val size: String,
        val aspect: Double,
    )

    @Serializable
    data class Focus(
        val x: Double,
        val y: Double,
    )
}
