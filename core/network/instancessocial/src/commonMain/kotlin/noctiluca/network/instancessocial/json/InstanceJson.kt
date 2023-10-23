package noctiluca.network.instancessocial.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InstanceJson(
    val id: String,
    val name: String,
    @SerialName("added_at")
    val addedAt: String?,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("checked_at")
    val checkedAt: String,
    val uptime: Int,
    val up: Boolean,
    val dead: Boolean,
    val version: String?,
    val ipv6: Boolean,
    @SerialName("https_score")
    val httpsScore: Int?,
    @SerialName("https_rank")
    val httpsRank: String?,
    @SerialName("obs_score")
    val obsScore: Int?,
    @SerialName("obs_rank")
    val obsRank: String?,
    val users: Int,
    val statuses: Int,
    val connections: Int,
    @SerialName("open_registrations")
    val openRegistrations: Boolean,
    val info: Info?,
    val thumbnail: String?,
    @SerialName("thumbnail_proxy")
    val thumbnailProxy: String?,
    @SerialName("active_users")
    val activeUsers: Int?,
    val email: String?,
    val admin: String?,
) {
    @Serializable
    data class Info(
        @SerialName("short_description")
        val shortDescription: String?,
        @SerialName("full_description")
        val fullDescription: String?,
        val topic: String?,
        val languages: List<String>?,
        @SerialName("other_languages_accepted")
        val otherLanguagesAccepted: Boolean,
        @SerialName("federates_with")
        val federatesWith: String?,
        @SerialName("prohibited_content")
        val prohibitedContent: List<String>,
        val categories: List<String>,
    )
}
