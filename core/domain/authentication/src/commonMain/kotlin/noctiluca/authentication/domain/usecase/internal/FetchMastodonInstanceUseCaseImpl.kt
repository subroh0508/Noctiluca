package noctiluca.authentication.domain.usecase.internal

import noctiluca.authentication.domain.usecase.FetchMastodonInstanceUseCase
import noctiluca.data.instance.InstanceRepository

internal class FetchMastodonInstanceUseCaseImpl(
    private val repository: InstanceRepository,
) : FetchMastodonInstanceUseCase {
    override suspend fun execute(domain: String) = repository.show(domain)
}
