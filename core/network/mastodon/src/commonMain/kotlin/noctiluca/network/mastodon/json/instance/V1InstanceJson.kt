package noctiluca.network.mastodon.json.instance

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import noctiluca.network.mastodon.json.account.AccountJson
import noctiluca.network.mastodon.json.rule.RuleJson

/**
 *  ref. https://docs.joinmastodon.org/entities/V1_Instance/
 */

@Serializable
data class V1InstanceJson(
    val uri: String,
    val title: String,
    @SerialName("short_description")
    val shortDescription: String,
    val description: String,
    val email: String,
    val version: String,
    val urls: Urls,
    val stats: Stats,
    val thumbnail: String?,
    val languages: List<String>,
    val registrations: Boolean,
    @SerialName("approval_required")
    val approvalRequired: Boolean,
    @SerialName("invites_enabled")
    val invitesEnabled: Boolean,
    val configuration: Configuration?,
    @SerialName("contact_account")
    val contactAccount: AccountJson,
    val rules: List<RuleJson>?,
) {
    @Serializable
    data class Urls(
        @SerialName("streaming_api")
        val streamingApi: String?,
    )

    @Serializable
    data class Stats(
        @SerialName("user_count")
        val userCount: Int?,
        @SerialName("status_count")
        val statusCount: Int?,
        @SerialName("domain_count")
        val domainCount: Int?,
    )

    @Serializable
    data class Configuration(
        val accounts: Accounts?, // Since 4.0.0
        val statuses: Statuses?,
        @SerialName("media_attachments")
        val mediaAttachments: MediaAttachments?,
        val polls: Polls?,
    ) {
        @Serializable
        data class Accounts(
            @SerialName("max_featured_tags")
            val maxFeaturedTags: Int?,
        )

        @Serializable
        data class Statuses(
            @SerialName("max_characters")
            val maxCharacters: Int?,
            @SerialName("max_media_attachments")
            val maxMediaAttachments: Int?,
            @SerialName("characters_reserved_per_url")
            val charactersReservedPerUrl: Int?,
        )

        @Serializable
        data class MediaAttachments(
            @SerialName("supported_mime_types")
            val supportedMimeTypes: List<String>?,
            @SerialName("image_size_limit")
            val imageSizeLimit: Int?,
            @SerialName("image_matrix_limit")
            val imageMatrixLimit: Int?,
            @SerialName("video_size_limit")
            val videoSizeLimit: Int?,
            @SerialName("video_frame_rate_limit")
            val videoFrameRateLimit: Int?,
            @SerialName("video_matrix_limit")
            val videoMatrixLimit: Int?,
        )

        @Serializable
        data class Polls(
            @SerialName("max_options")
            val maxOptions: Int?,
            @SerialName("max_characters_per_option")
            val maxCharactersPerOption: Int?,
            @SerialName("min_expiration")
            val minExpiration: Int?,
            @SerialName("max_expiration")
            val maxExpiration: Int?,
        )
    }
}
