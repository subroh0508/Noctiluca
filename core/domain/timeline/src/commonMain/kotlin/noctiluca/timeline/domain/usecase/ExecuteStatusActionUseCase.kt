package noctiluca.timeline.domain.usecase

import noctiluca.model.status.Status
import noctiluca.timeline.domain.model.StatusAction

interface ExecuteStatusActionUseCase {
    suspend fun execute(
        status: Status,
        action: StatusAction,
    )
}
