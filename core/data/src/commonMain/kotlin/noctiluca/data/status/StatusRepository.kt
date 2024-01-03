package noctiluca.data.status

import noctiluca.model.StatusId
import noctiluca.model.status.Status

interface StatusRepository {
    suspend fun fetch(id: StatusId): Status
    suspend fun fetchContext(id: StatusId): List<Status>
    suspend fun delete(id: StatusId): Status

    suspend fun favourite(status: Status): Status
    suspend fun boost(status: Status): Status
    suspend fun bookmark(status: Status): Status
}
