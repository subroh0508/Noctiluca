package noctiluca.timeline.domain.usecase

import kotlinx.coroutines.flow.Flow
import noctiluca.model.timeline.StreamEvent
import noctiluca.model.timeline.Timeline

interface FetchTimelineStreamUseCase {
    suspend fun execute(timeline: Timeline): Flow<StreamEvent>
}
