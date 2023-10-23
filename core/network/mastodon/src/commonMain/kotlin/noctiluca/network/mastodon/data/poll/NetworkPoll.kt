package noctiluca.network.mastodon.data.poll

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import noctiluca.network.mastodon.data.customemoji.NetworkCustomEmoji

/**
 *  ref. https://docs.joinmastodon.org/entities/Poll/
 */

@Serializable
data class NetworkPoll(
    val id: String,
    @SerialName("expires_at")
    val expiresAt: String,
    val expired: Boolean,
    val multiple: Boolean,
    @SerialName("votes_count")
    val votesCount: Int,
    @SerialName("voters_count")
    val votersCount: Int?,
    val options: List<Option>,
    val emojis: List<NetworkCustomEmoji>,
    val voted: Boolean?,
    @SerialName("own_votes")
    val ownVotes: List<Int>?,
) {
    @Serializable
    data class Option(
        val title: String,
        @SerialName("votes_count")
        val votesCount: Int?,
    )
}
