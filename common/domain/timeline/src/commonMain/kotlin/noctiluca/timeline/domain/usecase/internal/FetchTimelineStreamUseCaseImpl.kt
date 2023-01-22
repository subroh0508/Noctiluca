package noctiluca.timeline.domain.usecase.internal

import kotlinx.coroutines.flow.flow
import noctiluca.timeline.domain.model.Timeline
import noctiluca.timeline.domain.usecase.FetchTimelineStreamUseCase
import noctiluca.timeline.infra.repository.TimelineRepository

internal class FetchTimelineStreamUseCaseImpl(
    private val repository: TimelineRepository,
) : FetchTimelineStreamUseCase {
    override suspend fun execute(timeline: Timeline) = when (timeline) {
        is Timeline.Global -> repository.buildGlobalStream(
            onlyRemote = timeline.onlyRemote,
            onlyMedia = timeline.onlyMedia,
        )
        is Timeline.Local -> repository.buildLocalStream(
            onlyMedia = timeline.onlyMedia,
        )
        is Timeline.Home -> repository.buildHomeStream()
        is Timeline.HashTag -> flow {  }
        is Timeline.List -> flow {  }
    }
}