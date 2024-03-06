package noctiluca.data.timeline.impl

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import noctiluca.model.status.Status
import noctiluca.model.timeline.StreamEvent
import noctiluca.model.timeline.Timeline
import noctiluca.model.timeline.TimelineId
import noctiluca.model.timeline.TimelineStreamState

@Suppress("TooManyFunctions")
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

    internal fun hasActiveJob(
        timelineId: TimelineId,
    ) = mutableStateFlow.value.hasActiveJob(timelineId)

    internal fun timeline(
        timelineId: TimelineId,
    ) = mutableStateFlow.value.timeline(timelineId)

    internal fun favourite(
        status: Status,
    ) = execute { timeline -> timeline.favourite(status) }

    internal fun boost(
        status: Status,
    ) = execute { timeline -> timeline.boost(status) }

    internal fun bookmark(
        status: Status,
    ) = execute { timeline -> timeline.bookmark(status) }

    internal fun update(
        status: Status,
    ) = execute { timeline -> timeline.replace(status) }

    private inline fun execute(
        crossinline action: (Timeline) -> Timeline,
    ) {
        mutableStateFlow.value += mutableStateFlow.value.map { timelineId, timeline, _ ->
            timelineId to action(timeline)
        }
    }
}
