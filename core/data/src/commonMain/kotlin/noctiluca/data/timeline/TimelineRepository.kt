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

    fun load(
        timelineId: TimelineId,
        timeline: Timeline,
    )

    fun favourite(status: Status)

    fun boost(status: Status)

    fun bookmark(status: Status)
}
