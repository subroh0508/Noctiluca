package noctiluca.timeline.domain.usecase

import kotlinx.coroutines.Deferred
import noctiluca.timeline.domain.model.Timeline

interface UpdateTimelineUseCase {
    //suspend fun execute(initial: List<Timeline>): List<Timeline>
    suspend fun execute(initial: List<Timeline>): List<Deferred<Timeline>>
}
