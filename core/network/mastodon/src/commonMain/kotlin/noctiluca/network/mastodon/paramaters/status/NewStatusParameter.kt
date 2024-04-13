package noctiluca.network.mastodon.paramaters.status

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewStatusParameter(
    val status: String,
    @SerialName("media_ids")
    val mediaIds: List<String>,
    val poll: Poll?,
    @SerialName("in_reply_to_id")
    val inReplyToId: String?,
    val sensitive: Boolean,
    @SerialName("spoiler_text")
    val spoilerText: String?,
    val visibility: String,
    val language: String,
    @SerialName("scheduled_at")
    val scheduledAt: String?,
) {
    @Serializable
    data class Poll(
        val options: List<Int>,
        @SerialName("expires_in")
        val expiresIn: Int,
        val multiple: Boolean,
        @SerialName("hide_totals")
        val hideTotals: Boolean,
    )
}
