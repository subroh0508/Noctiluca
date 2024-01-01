package noctiluca.timeline.domain.usecase.internal

import noctiluca.data.status.StatusIndexRepository
import noctiluca.data.timeline.TimelineRepository
import noctiluca.model.timeline.TimelineId
import noctiluca.timeline.domain.usecase.LoadTimelineStatusesUseCase

internal class LoadTimelineStatusesUseCaseImpl(
    private val timelineRepository: TimelineRepository,
    private val statusIndexRepository: StatusIndexRepository,
) : LoadTimelineStatusesUseCase {
    override suspend fun execute(timelineId: TimelineId) {
        val timeline = timelineRepository[timelineId] ?: return

        timelineRepository.load(
            timelineId,
            timeline + statusIndexRepository.fetchStatuses(timeline),
        )
    }
}
