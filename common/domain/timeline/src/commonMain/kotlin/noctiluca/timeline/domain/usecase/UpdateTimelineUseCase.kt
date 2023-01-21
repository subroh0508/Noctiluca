package noctiluca.timeline.domain.usecase

import noctiluca.timeline.domain.model.Timeline

interface UpdateTimelineUseCase {
    //suspend fun execute(initial: List<Timeline>): List<Timeline>
    suspend fun execute(timeline: Timeline): Timeline
}
