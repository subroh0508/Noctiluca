package noctiluca.authentication.domain.usecase.internal

import noctiluca.authentication.domain.usecase.SearchMastodonInstancesUseCase
import noctiluca.instance.infra.repository.InstanceRepository

internal class SearchMastodonInstancesUseCaseImpl(
    private val repository: InstanceRepository,
) : SearchMastodonInstancesUseCase {
    override suspend fun execute(query: String) = repository.search(query)
}
