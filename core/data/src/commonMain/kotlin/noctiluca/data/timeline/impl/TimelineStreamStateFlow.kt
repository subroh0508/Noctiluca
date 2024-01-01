package noctiluca.data.timeline.impl

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import noctiluca.model.timeline.StreamEvent
import noctiluca.model.timeline.Timeline
import noctiluca.model.timeline.TimelineId
import noctiluca.model.timeline.TimelineStreamState

internal class TimelineStreamStateFlow(
    private val state: TimelineStreamState,
) : MutableStateFlow<TimelineStreamState> by MutableStateFlow(state) {
    operator fun set(
        timelineId: TimelineId,
        timeline: Timeline,
    ) {
        value += (timelineId to timeline)
    }

    operator fun set(
        timelineId: TimelineId,
        job: Job,
    ) {
        value += (timelineId to job)
    }

    operator fun set(
        timelineId: TimelineId,
        event: StreamEvent,
    ) {
        value += (timelineId to event)
    }

    fun hasActiveJob(timelineId: TimelineId) = value.hasActiveJob(timelineId)

    fun cancelAll() = value.cancelAll()
    fun clear() {
        value = TimelineStreamState()
    }
}
