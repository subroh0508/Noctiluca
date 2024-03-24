package noctiluca.model.timeline

import kotlinx.coroutines.Job
import kotlin.jvm.JvmName

data class TimelineStreamState(
    private val timeline: Map<TimelineId, Timeline> = emptyMap(),
    private val stream: Map<TimelineId, Job> = emptyMap(),
    private val latestEvent: Map<TimelineId, StreamEvent> = emptyMap(),
) {
    fun timeline(timelineId: TimelineId) = timeline[timelineId]
    fun latestEvent(timelineId: TimelineId) = latestEvent[timelineId]

    fun hasActiveJob(timelineId: TimelineId) = stream[timelineId]?.isActive == true

    fun <R> map(
        transform: (TimelineId, Timeline, StreamEvent?) -> Pair<TimelineId, R>
    ) = timeline.map { (id, timeline) ->
        transform(id, timeline, latestEvent[id])
    }.toMap()

    @JvmName("plusTimelineMap")
    operator fun plus(timeline: Map<TimelineId, Timeline>) = copy(timeline = this.timeline + timeline)

    @JvmName("plusStreamEventMap")
    operator fun plus(latestEvent: Map<TimelineId, StreamEvent>) = copy(latestEvent = this.latestEvent + latestEvent)

    @JvmName("plusTimelinePair")
    operator fun plus(timeline: Pair<TimelineId, Timeline>) = copy(timeline = this.timeline + timeline)

    @JvmName("plusJobPair")
    operator fun plus(stream: Pair<TimelineId, Job>) = copy(
        timeline = this.timeline.mapValues { (id, timeline) ->
            if (id == stream.first) {
                timeline.activate(true)
            } else {
                timeline
            }
        },
        stream = this.stream + stream,
    )

    @JvmName("plusStreamEventPair")
    operator fun plus(latestEvent: Pair<TimelineId, StreamEvent>) = copy(latestEvent = this.latestEvent + latestEvent)
}
