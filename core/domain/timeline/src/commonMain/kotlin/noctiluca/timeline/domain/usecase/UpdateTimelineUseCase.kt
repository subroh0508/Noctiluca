package noctiluca.timeline.domain.usecase

import noctiluca.model.timeline.Timeline

interface UpdateTimelineUseCase {
    // suspend fun execute(initial: List<Timeline>): List<Timeline>
    suspend fun execute(timeline: Timeline): Timeline
}
