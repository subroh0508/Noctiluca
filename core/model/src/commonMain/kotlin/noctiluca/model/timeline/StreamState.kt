package noctiluca.model.timeline

import kotlinx.coroutines.Job

data class StreamState(
    private val timeline: Map<TimelineId, Timeline> = emptyMap(),
    private val stream: Map<TimelineId, Job> = emptyMap(),
    private val latestEvent: Map<TimelineId, StreamEvent> = emptyMap(),
) {
    fun timeline(timelineId: TimelineId) = timeline[timelineId]
    fun latestEvent(timelineId: TimelineId) = latestEvent[timelineId]

    fun hasActiveJob(timelineId: TimelineId) = stream[timelineId]?.isActive == true

    fun cancel(timelineId: TimelineId) = stream[timelineId]?.cancel()
    fun cancelAll() = stream.forEach { (id) -> cancel(id) }

    fun <R> map(
        transform: (TimelineId, Timeline, StreamEvent?) -> Pair<TimelineId, R>
    ) = timeline.map { (id, timeline) ->
        transform(id, timeline, latestEvent[id])
    }.toMap()

    @JvmName("plusTimelineMap")
    operator fun plus(timeline: Map<TimelineId, Timeline>) = copy(timeline = this.timeline + timeline)

    @JvmName("plusJobMap")
    operator fun plus(stream: Map<TimelineId, Job>) = copy(stream = this.stream + stream)

    @JvmName("plusStreamEventMap")
    operator fun plus(latestEvent: Map<TimelineId, StreamEvent>) = copy(latestEvent = this.latestEvent + latestEvent)

    @JvmName("plusTimelinePair")
    operator fun plus(timeline: Pair<TimelineId, Timeline>) = copy(timeline = this.timeline + timeline)

    @JvmName("plusJobPair")
    operator fun plus(stream: Pair<TimelineId, Job>) = copy(stream = this.stream + stream)

    @JvmName("plusStreamEventPair")
    operator fun plus(latestEvent: Pair<TimelineId, StreamEvent>) = copy(latestEvent = this.latestEvent + latestEvent)
}
