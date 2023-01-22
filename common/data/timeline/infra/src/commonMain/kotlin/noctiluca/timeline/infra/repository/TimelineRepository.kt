package noctiluca.timeline.infra.repository

import kotlinx.coroutines.flow.Flow
import noctiluca.model.StatusId
import noctiluca.status.model.Status
import noctiluca.timeline.model.StreamEvent

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
}