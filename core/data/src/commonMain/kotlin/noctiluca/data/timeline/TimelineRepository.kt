package noctiluca.data.timeline

import kotlinx.coroutines.flow.Flow
import noctiluca.model.status.Status
import noctiluca.model.timeline.Timeline
import noctiluca.model.timeline.TimelineId
import noctiluca.model.timeline.TimelineStreamState

interface TimelineRepository {
    val stream: Flow<TimelineStreamState>

    operator fun get(timelineId: TimelineId): Timeline?

    suspend fun fetchInitialTimeline(): Map<TimelineId, Timeline>

    suspend fun subscribe(
        initial: Map<TimelineId, Timeline>,
        statuses: Map<TimelineId, List<Status>>,
    )

    suspend fun load(
        timelineId: TimelineId,
        timeline: Timeline,
    )

    fun unsubscribe()
}
