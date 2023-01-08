package noctiluca.timeline.domain.usecase

import noctiluca.timeline.domain.model.Timeline

interface UpdateTimelineUseCase {
    suspend fun execute(current: List<Timeline>): List<Timeline>
}