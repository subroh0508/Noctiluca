package noctiluca.timeline.infra.repository

import noctiluca.model.StatusId
import noctiluca.status.model.Status

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
}