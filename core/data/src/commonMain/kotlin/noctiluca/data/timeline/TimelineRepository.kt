package noctiluca.data.timeline

import kotlinx.coroutines.flow.Flow
import noctiluca.model.timeline.TimelineId
import noctiluca.model.timeline.TimelineStreamState

interface TimelineRepository {
    fun buildStream(): Flow<TimelineStreamState>

    suspend fun start()
    suspend fun load(timelineId: TimelineId)
    suspend fun close()
}
