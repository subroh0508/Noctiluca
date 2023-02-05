package noctiluca.timeline.domain.usecase.internal

import noctiluca.status.infra.repository.StatusRepository
import noctiluca.status.model.Status
import noctiluca.timeline.domain.model.StatusAction
import noctiluca.timeline.domain.usecase.ExecuteStatusActionUseCase

internal class ExecuteStatusActionUseCaseImpl(
    private val repository: StatusRepository,
) : ExecuteStatusActionUseCase {
    override suspend fun execute(
        status: Status,
        action: StatusAction,
    ) = when (action) {
        StatusAction.FAVOURITE -> repository.favourite(status)
        StatusAction.BOOST -> repository.boost(status)
        StatusAction.BOOKMARK -> repository.bookmark(status)
    }
}
