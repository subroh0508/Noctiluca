package noctiluca.data.timeline.impl

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import noctiluca.model.timeline.StreamEvent
import noctiluca.model.timeline.Timeline
import noctiluca.model.timeline.TimelineId
import noctiluca.model.timeline.TimelineStreamState

class TimelineStreamStateFlow(
    private val mutableStateFlow: MutableStateFlow<TimelineStreamState>,
) : Flow<TimelineStreamState> by mutableStateFlow {
    constructor(state: TimelineStreamState) : this(MutableStateFlow(state))

    internal operator fun set(
        timelineId: TimelineId,
        timeline: Timeline,
    ) {
        mutableStateFlow.value += (timelineId to timeline)
    }

    internal operator fun set(
        timelineId: TimelineId,
        job: Job,
    ) {
        mutableStateFlow.value += (timelineId to job)
    }

    internal operator fun set(
        timelineId: TimelineId,
        event: StreamEvent,
    ) {
        mutableStateFlow.value += (timelineId to event)
    }

    internal fun setInitialTimeline(
        initial: Map<TimelineId, Timeline>,
    ) {
        mutableStateFlow.value = TimelineStreamState(timeline = initial)
    }

    internal fun hasActiveJob(
        timelineId: TimelineId,
    ) = mutableStateFlow.value.hasActiveJob(timelineId)

    internal fun timeline(
        timelineId: TimelineId,
    ) = mutableStateFlow.value.timeline(timelineId)

    internal fun cancelAll() = mutableStateFlow.value.cancelAll()
    internal fun clear() {
        mutableStateFlow.value = TimelineStreamState()
    }
}
