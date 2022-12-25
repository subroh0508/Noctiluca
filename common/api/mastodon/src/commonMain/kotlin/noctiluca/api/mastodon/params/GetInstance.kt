package noctiluca.api.mastodon.params

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

abstract class GetInstance {
    companion object {
        internal const val ENDPOINT = "/api/v1/instance"
    }

    @Serializable
    data class Response(
        val title: String,
        val uri: String,
        val version: String,
        @SerialName("short_description")
        val shortDescription: String,
        val stats: Stats,
        val thumbnail: String?,
        val languages: List<String>,
    ) {
        @Serializable
        data class Stats(
            @SerialName("user_count")
            val userCount: Int,
            @SerialName("status_count")
            val statusCount: Int,
        )
    }
}
