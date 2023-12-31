package noctiluca.timeline.domain.usecase.internal

import noctiluca.data.timeline.TimelineRepository
import noctiluca.model.status.StatusList
import noctiluca.model.timeline.Timeline
import noctiluca.timeline.domain.usecase.UpdateTimelineUseCase

internal class UpdateTimelineUseCaseImpl(
    private val repository: TimelineRepository,
) : UpdateTimelineUseCase {
    override suspend fun execute(
        timeline: Timeline,
    ) = timeline + fetchStatuses(timeline)

    private suspend fun fetchStatuses(
        timeline: Timeline,
    ): StatusList {
        val maxId = timeline.statuses.lastOrNull()?.id

        return when (timeline) {
            is Timeline.Global -> repository.fetchGlobal(
                timeline.onlyRemote,
                timeline.onlyMedia,
                maxId = maxId,
            )
            is Timeline.Local -> repository.fetchLocal(
                timeline.onlyMedia,
                maxId = maxId,
            )
            is Timeline.Home -> repository.fetchHome(
                maxId = maxId,
            )
            is Timeline.HashTag -> listOf()
            is Timeline.List -> listOf()
        }
    }
}
