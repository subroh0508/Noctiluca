package noctiluca.timeline.domain.usecase

import kotlinx.coroutines.flow.Flow
import noctiluca.model.timeline.Timeline
import noctiluca.model.timeline.TimelineId
import noctiluca.model.timeline.TimelineStreamState

interface SubscribeTimelineStreamUseCase {
    suspend fun execute(timelines: Map<TimelineId, Timeline>): Flow<TimelineStreamState>
}
