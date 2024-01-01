package noctiluca.data.timeline

import noctiluca.data.timeline.impl.TimelineStreamStateFlow
import noctiluca.model.status.Status
import noctiluca.model.timeline.Timeline
import noctiluca.model.timeline.TimelineId

interface TimelineRepository {
    val stream: TimelineStreamStateFlow

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
