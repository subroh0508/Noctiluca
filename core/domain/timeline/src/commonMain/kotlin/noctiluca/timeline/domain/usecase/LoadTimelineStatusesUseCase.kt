package noctiluca.timeline.domain.usecase

import noctiluca.model.timeline.TimelineId

interface LoadTimelineStatusesUseCase {
    suspend fun execute(timelineId: TimelineId)
}
