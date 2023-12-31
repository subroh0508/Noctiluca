package noctiluca.data.timeline

import kotlinx.coroutines.flow.Flow
import noctiluca.model.timeline.Timeline
import noctiluca.model.timeline.TimelineId

interface TimelineRepository {
    fun buildStream(): Flow<Map<TimelineId, Timeline>>

    suspend fun start()
    suspend fun load(timelineId: TimelineId)
    suspend fun close()
}
