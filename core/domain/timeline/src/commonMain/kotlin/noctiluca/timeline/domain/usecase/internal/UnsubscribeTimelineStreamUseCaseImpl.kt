package noctiluca.timeline.domain.usecase.internal

import noctiluca.data.timeline.TimelineRepository
import noctiluca.timeline.domain.usecase.UnsubscribeTimelineStreamUseCase

internal class UnsubscribeTimelineStreamUseCaseImpl(
    private val timelineRepository: TimelineRepository,
) : UnsubscribeTimelineStreamUseCase {
    override fun execute() = timelineRepository.unsubscribe()
}
