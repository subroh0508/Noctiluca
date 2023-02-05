package noctiluca.authentication.domain.usecase.internal

import noctiluca.authentication.domain.usecase.ShowMastodonInstanceUseCase
import noctiluca.instance.infra.repository.InstanceRepository

internal class ShowMastodonInstanceUseCaseImpl(
    private val repository: InstanceRepository,
) : ShowMastodonInstanceUseCase {
    override suspend fun execute(domain: String) = repository.show(domain)
}
