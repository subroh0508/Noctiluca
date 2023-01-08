package noctiluca.timeline.domain.usecase.internal

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import noctiluca.status.model.StatusList
import noctiluca.timeline.domain.model.Timeline
import noctiluca.timeline.domain.usecase.UpdateTimelineUseCase
import noctiluca.timeline.infra.repository.TimelineRepository

internal class UpdateTimelineUseCaseImpl(
    private val repository: TimelineRepository
) : UpdateTimelineUseCase {
    override suspend fun execute(
        initial: List<Timeline>,
    ) = withContext(Dispatchers.Default) {
        initial.map {
            if (it.statuses.isEmpty()) {
                return@map async { it + fetchInitialStatuses(it) }
            }

            async { it }
        }
    }

    private suspend fun fetchInitialStatuses(
        timeline: Timeline,
    ): StatusList {
        val statuses = when (timeline) {
            is Timeline.Global -> repository.fetchGlobal(
                timeline.onlyRemote,
                timeline.onlyMedia,
            )
            is Timeline.Local -> repository.fetchLocal(
                timeline.onlyMedia,
            )
            is Timeline.Home -> listOf()
            is Timeline.HashTag -> listOf()
            is Timeline.List -> listOf()
        }

        return statuses + timeline.statuses
    }
}
