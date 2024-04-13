package noctiluca.data.status

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime
import noctiluca.model.StatusId
import noctiluca.model.status.Poll
import noctiluca.model.status.Status
import noctiluca.model.status.StatusList

interface StatusRepository {
    fun context(id: StatusId): Flow<StatusList>

    suspend fun new(
        status: String,
        spoilerText: String?,
        visibility: Status.Visibility,
        mediaIds: List<String> = listOf(),
        poll: Poll.New? = null,
        inReplyToId: StatusId? = null,
        sensitive: Boolean = false,
        language: String = "ja",
        scheduledAt: LocalDateTime? = null,
    ): Status

    suspend fun delete(id: StatusId): Status

    suspend fun favourite(status: Status): Status
    suspend fun boost(status: Status): Status
    suspend fun bookmark(status: Status): Status
}
