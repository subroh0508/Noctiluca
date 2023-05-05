package noctiluca.authentication.domain.usecase

import noctiluca.model.StatusId
import noctiluca.status.model.Status

interface FetchLocalTimelineUseCase {
    suspend fun execute(
        domain: String,
        maxId: StatusId? = null,
    ): List<Status>
}
