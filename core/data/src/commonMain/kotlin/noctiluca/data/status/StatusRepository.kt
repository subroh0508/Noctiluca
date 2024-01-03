package noctiluca.data.status

import kotlinx.coroutines.flow.Flow
import noctiluca.model.StatusId
import noctiluca.model.status.Status
import noctiluca.model.status.StatusList

interface StatusRepository {
    fun context(id: StatusId): Flow<StatusList>

    suspend fun delete(id: StatusId): Status

    suspend fun favourite(status: Status): Status
    suspend fun boost(status: Status): Status
    suspend fun bookmark(status: Status): Status
}
