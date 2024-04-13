package noctiluca.model.status

import kotlinx.datetime.LocalDateTime
import noctiluca.model.PollId

data class Poll(
    val id: PollId,
    val expiresAt: LocalDateTime,
    val expired: Boolean,
    val multiple: Boolean,
    val votesCount: Int,
    val votersCount: Int,
    val options: List<Option>,
    // val emojis: List<CustomEmoji>,
    val voted: Boolean,
    val ownVoted: List<Int>,
) {
    data class Option(
        val title: String,
        val votesCount: Int,
    )

    data class New(
        val options: List<Int>,
        val expiresIn: Int,
        val multiple: Boolean,
        val hideTotals: Boolean,
    )
}
