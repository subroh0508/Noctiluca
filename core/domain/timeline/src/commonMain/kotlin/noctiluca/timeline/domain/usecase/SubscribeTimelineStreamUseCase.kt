package noctiluca.timeline.domain.usecase

import kotlinx.coroutines.flow.Flow
import noctiluca.model.timeline.TimelineStreamState

interface SubscribeTimelineStreamUseCase {
    suspend fun execute(): Flow<TimelineStreamState>
}
