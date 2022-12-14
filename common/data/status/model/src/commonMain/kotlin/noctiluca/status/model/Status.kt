package noctiluca.status.model

import kotlinx.datetime.LocalDateTime
import noctiluca.model.StatusId

data class Status(
    val id: StatusId,
    val content: String,
    val warningText: String?,
    val createdAt: LocalDateTime,
    val visibility: Visibility,
    val repliesCount: Int,
    val favouriteCount: Int,
    val reblogCount: Int,
    val favourited: Boolean,
    val reblogged: Boolean,
    val tooter: Tooter,
    val rebloggedBy: Tooter?,
) {
    enum class Visibility { PUBLIC, UNLISTED, PRIVATE, DIRECT }

    val isWarningContent get() = warningText?.isNotBlank() == true
}
