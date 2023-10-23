package noctiluca.data.status

import noctiluca.model.status.Status

interface StatusRepository {
    suspend fun favourite(status: Status): Status
    suspend fun boost(status: Status): Status
    suspend fun bookmark(status: Status): Status
}
