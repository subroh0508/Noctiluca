package noctiluca.data.timeline

import kotlinx.coroutines.flow.Flow
import noctiluca.model.timeline.StreamState
import noctiluca.model.timeline.TimelineId

interface TimelineRepository {
    fun buildStream(): Flow<StreamState>

    suspend fun start()
    suspend fun load(timelineId: TimelineId)
    suspend fun close()
}
