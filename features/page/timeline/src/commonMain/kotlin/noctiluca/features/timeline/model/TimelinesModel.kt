package noctiluca.features.timeline.model

import noctiluca.features.shared.model.LoadState
import noctiluca.model.timeline.StreamEvent
import noctiluca.model.timeline.Timeline
import noctiluca.model.timeline.TimelineId
import noctiluca.model.timeline.TimelineStreamState

data class TimelinesModel(
    val tabs: Map<TimelineId, State> = mapOf(),
    val loadState: Map<TimelineId, LoadState> = mapOf(),
) {
    constructor(
        timelineStreamState: TimelineStreamState,
        foregroundTimelineId: TimelineId,
        loadState: Map<TimelineId, LoadState>,
    ) : this(
        tabs = timelineStreamState.map { id, timeline, latestEvent ->
            id to State(
                timeline = timeline.activate(timelineStreamState.hasActiveJob(id)),
                latestEvent = latestEvent,
                foreground = foregroundTimelineId == id,
            )
        },
        loadState = loadState,
    )

    data class State(
        val timeline: Timeline,
        val latestEvent: StreamEvent? = null,
        val foreground: Boolean = false,
    ) {
        val isActive get() = timeline.isActive
    }

    val foreground get() = tabs.values.find { it.foreground }
    val currentTabIndex get() = tabs.values.indexOfFirst { it.foreground }

    fun toTabList() = tabs.values.toList()
    fun findTimelineId(index: Int) = tabs.keys.toList()[index]

    fun inactivates() = tabs
        .filterNot { (_, tab) -> tab.isActive }
        .mapValues { (_, tab) -> tab.timeline }
}
