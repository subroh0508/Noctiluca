package noctiluca.status.infra.repository

import noctiluca.status.model.Status

interface StatusRepository {
    suspend fun favourite(status: Status): Status
    suspend fun boost(status: Status): Status
    suspend fun bookmark(status: Status): Status
}
