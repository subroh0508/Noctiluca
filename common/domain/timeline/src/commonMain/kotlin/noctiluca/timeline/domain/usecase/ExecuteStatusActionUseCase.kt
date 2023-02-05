package noctiluca.timeline.domain.usecase

import noctiluca.status.model.Status
import noctiluca.timeline.domain.model.StatusAction

interface ExecuteStatusActionUseCase {
    suspend fun execute(
        status: Status,
        action: StatusAction,
    ): Status
}
