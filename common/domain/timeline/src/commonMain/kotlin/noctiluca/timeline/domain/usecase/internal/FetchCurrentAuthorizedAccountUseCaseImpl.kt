package noctiluca.timeline.domain.usecase.internal

import noctiluca.account.infra.repository.AuthorizedAccountRepository
import noctiluca.timeline.domain.usecase.FetchCurrentAuthorizedAccountUseCase

internal class FetchCurrentAuthorizedAccountUseCaseImpl(
    private val repository: AuthorizedAccountRepository,
) : FetchCurrentAuthorizedAccountUseCase {
    override suspend fun execute() = repository.fetchCurrent()
}
