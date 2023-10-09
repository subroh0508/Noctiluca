package noctiluca.timeline.domain.usecase

import kotlinx.coroutines.flow.Flow
import noctiluca.model.timeline.StreamEvent
import noctiluca.timeline.domain.model.Timeline

interface FetchTimelineStreamUseCase {
    suspend fun execute(timeline: Timeline): Flow<StreamEvent>
}
