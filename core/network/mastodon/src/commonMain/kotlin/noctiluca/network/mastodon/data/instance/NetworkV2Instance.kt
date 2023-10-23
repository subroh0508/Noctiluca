package noctiluca.network.mastodon.data.instance

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import noctiluca.network.mastodon.data.account.NetworkAccount
import noctiluca.network.mastodon.data.rule.NetworkRule

/**
 *  ref. https://docs.joinmastodon.org/entities/Instance/
 */

@Serializable
data class NetworkV2Instance(
    val domain: String,
    val title: String,
    val version: String,
    @SerialName("source_url")
    val sourceUrl: String,
    val description: String,
    val usage: Usage,
    val thumbnail: Thumbnail,
    val languages: List<String>,
    val configuration: Configuration,
    val registrations: Registrations,
    val contact: Contact,
    val rules: List<NetworkRule>,
) {
    @Serializable
    data class Usage(
        val users: Users,
    ) {
        @Serializable
        data class Users(
            @SerialName("active_month")
            val activeMonth: Int,
        )
    }

    @Serializable
    data class Thumbnail(
        val url: String,
        val blurhash: String?,
        val versions: Versions?,
    ) {
        @Serializable
        data class Versions(
            @SerialName("@1x")
            val x1: String,
            @SerialName("@2x")
            val x2: String,
        )
    }

    @Serializable
    data class Configuration(
        val urls: Urls,
        val accounts: Accounts,
        val statuses: Statuses,
        @SerialName("media_attachments")
        val mediaAttachments: MediaAttachments,
        val polls: Polls,
        val translation: Translation,
    ) {
        @Serializable
        data class Urls(
            val streaming: String,
        )

        @Serializable
        data class Accounts(
            @SerialName("max_featured_tags")
            val maxFeaturedTags: Int,
        )

        @Serializable
        data class Statuses(
            @SerialName("max_characters")
            val maxCharacters: Int,
            @SerialName("max_media_attachments")
            val maxMediaAttachments: Int,
            @SerialName("characters_reserved_per_url")
            val charactersReservedPerUrl: Int,
        )

        @Serializable
        data class MediaAttachments(
            @SerialName("supported_mime_types")
            val supportedMimeTypes: List<String>,
            @SerialName("image_size_limit")
            val imageSizeLimit: Int,
            @SerialName("image_matrix_limit")
            val imageMatrixLimit: Int,
            @SerialName("video_size_limit")
            val videoSizeLimit: Int,
            @SerialName("video_frame_rate_limit")
            val videoFrameRateLimit: Int,
        )

        @Serializable
        data class Polls(
            @SerialName("max_options")
            val maxOptions: Int,
            @SerialName("max_characters_per_option")
            val maxCharactersPerOption: Int,
            @SerialName("min_expiration")
            val minExpiration: Int,
            @SerialName("max_expiration")
            val maxExpiration: Int,
        )

        @Serializable
        data class Translation(
            val enabled: Boolean,
        )
    }

    @Serializable
    data class Registrations(
        val enabled: Boolean,
        @SerialName("approval_required")
        val approvalRequired: Boolean,
        val message: String?,
    )

    @Serializable
    data class Contact(
        val email: String,
        val account: NetworkAccount,
    )
}
