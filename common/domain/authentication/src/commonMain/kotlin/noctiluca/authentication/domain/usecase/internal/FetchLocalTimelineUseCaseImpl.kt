package noctiluca.authentication.domain.usecase.internal

import noctiluca.authentication.domain.usecase.FetchLocalTimelineUseCase
import noctiluca.instance.infra.repository.InstanceRepository
import noctiluca.model.StatusId

internal class FetchLocalTimelineUseCaseImpl(
    private val repository: InstanceRepository,
) : FetchLocalTimelineUseCase {
    override suspend fun execute(
        domain: String,
        maxId: StatusId?,
    ) = repository.fetchLocalTimeline(domain, maxId)
}
