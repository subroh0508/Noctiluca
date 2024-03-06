package noctiluca.timeline.domain.usecase.internal

import kotlinx.coroutines.flow.Flow
import noctiluca.data.status.StatusIndexRepository
import noctiluca.data.timeline.TimelineRepository
import noctiluca.model.timeline.Timeline
import noctiluca.model.timeline.TimelineId
import noctiluca.model.timeline.TimelineStreamState
import noctiluca.timeline.domain.usecase.SubscribeTimelineStreamUseCase

internal class SubscribeTimelineStreamUseCaseImpl(
    private val timelineRepository: TimelineRepository,
    private val statusIndexRepository: StatusIndexRepository,
) : SubscribeTimelineStreamUseCase {
    override suspend fun execute(
        timelines: Map<TimelineId, Timeline>,
    ): Flow<TimelineStreamState> {
        val initial = timelines.takeIf { it.isNotEmpty() }
            ?: timelineRepository.fetchInitialTimeline()
        val statuses = initial.map { (timelineId, timeline) ->
            timelineId to statusIndexRepository.fetchStatuses(timeline)
        }.toMap()

        timelineRepository.subscribe(initial, statuses)

        return timelineRepository.stream
    }
}
