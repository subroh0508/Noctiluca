package noctiluca.timeline.domain.usecase.internal

import noctiluca.data.status.StatusIndexRepository
import noctiluca.model.timeline.Timeline

internal suspend fun StatusIndexRepository.fetchStatuses(
    timeline: Timeline,
) = when (timeline) {
    is Timeline.Global -> fetchGlobal(
        timeline.onlyRemote,
        timeline.onlyMedia,
        timeline.maxId,
    )

    is Timeline.Local -> fetchLocal(
        timeline.onlyMedia,
        timeline.maxId,
    )

    is Timeline.Home -> fetchHome(
        timeline.maxId,
    )

    is Timeline.HashTag -> listOf()
    is Timeline.List -> listOf()
}
