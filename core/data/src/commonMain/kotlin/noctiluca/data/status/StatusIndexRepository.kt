package noctiluca.data.status

import noctiluca.model.StatusId
import noctiluca.model.status.Status

interface StatusIndexRepository {
    suspend fun fetchGlobal(
        onlyRemote: Boolean,
        onlyMedia: Boolean,
        maxId: StatusId? = null,
    ): List<Status>

    suspend fun fetchLocal(
        onlyMedia: Boolean,
        maxId: StatusId? = null,
    ): List<Status>

    suspend fun fetchHome(
        maxId: StatusId? = null,
    ): List<Status>
}
