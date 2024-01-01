package noctiluca.data.timeline.impl

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import noctiluca.model.timeline.StreamEvent
import noctiluca.model.timeline.StreamState
import noctiluca.model.timeline.Timeline
import noctiluca.model.timeline.TimelineId

internal class StreamStateFlow(
    private val state: StreamState,
) : MutableStateFlow<StreamState> by MutableStateFlow(state) {
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

    fun clearTimeline() {
        value = value.copy(timeline = emptyMap())
    }

    fun cancelAll() {
        value.cancelAll()
        value = value.copy(stream = emptyMap(), latestEvent = emptyMap())
    }
}
