package noctiluca.data.timeline

import kotlinx.coroutines.flow.Flow
import noctiluca.model.StatusId
import noctiluca.model.status.Status
import noctiluca.model.timeline.StreamEvent

interface TimelineRepository {
    suspend fun fetchGlobal(
        onlyRemote: Boolean = false,
        onlyMedia: Boolean = false,
        maxId: StatusId? = null,
    ): List<Status>

    suspend fun fetchLocal(
        onlyMedia: Boolean = false,
        maxId: StatusId? = null,
    ): List<Status>

    suspend fun fetchHome(
        maxId: StatusId? = null,
    ): List<Status>

    suspend fun buildGlobalStream(
        onlyRemote: Boolean = false,
        onlyMedia: Boolean = false,
    ): Flow<StreamEvent>

    suspend fun buildLocalStream(
        onlyMedia: Boolean = false,
    ): Flow<StreamEvent>

    suspend fun buildHomeStream(): Flow<StreamEvent>
}
