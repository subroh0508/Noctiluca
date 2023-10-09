package noctiluca.authentication.domain.usecase

import noctiluca.model.StatusId
import noctiluca.model.status.Status

interface FetchLocalTimelineUseCase {
    suspend fun execute(
        domain: String,
        maxId: StatusId? = null,
    ): List<Status>
}
