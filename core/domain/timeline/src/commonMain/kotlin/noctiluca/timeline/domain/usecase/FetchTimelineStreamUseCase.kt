package noctiluca.timeline.domain.usecase

import kotlinx.coroutines.flow.Flow
import noctiluca.timeline.domain.model.Timeline
import noctiluca.timeline.model.StreamEvent

interface FetchTimelineStreamUseCase {
    suspend fun execute(timeline: Timeline): Flow<StreamEvent>
}
