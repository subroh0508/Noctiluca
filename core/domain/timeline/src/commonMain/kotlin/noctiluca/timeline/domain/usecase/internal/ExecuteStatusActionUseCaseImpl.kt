package noctiluca.timeline.domain.usecase.internal

import noctiluca.data.status.StatusRepository
import noctiluca.data.timeline.TimelineRepository
import noctiluca.model.status.Status
import noctiluca.timeline.domain.model.StatusAction
import noctiluca.timeline.domain.usecase.ExecuteStatusActionUseCase

internal class ExecuteStatusActionUseCaseImpl(
    private val timelineRepository: TimelineRepository,
    private val statusRepository: StatusRepository,
) : ExecuteStatusActionUseCase {
    override suspend fun execute(
        status: Status,
        action: StatusAction,
    ) = when (action) {
        StatusAction.FAVOURITE -> favourite(status)
        StatusAction.BOOST -> boost(status)
        StatusAction.BOOKMARK -> bookmark(status)
    }

    private suspend fun favourite(status: Status): Status {
        timelineRepository.favourite(status)
        return statusRepository.favourite(status)
    }

    private suspend fun boost(status: Status): Status {
        timelineRepository.boost(status)
        return statusRepository.boost(status)
    }

    private suspend fun bookmark(status: Status): Status {
        timelineRepository.bookmark(status)
        return statusRepository.bookmark(status)
    }
}
