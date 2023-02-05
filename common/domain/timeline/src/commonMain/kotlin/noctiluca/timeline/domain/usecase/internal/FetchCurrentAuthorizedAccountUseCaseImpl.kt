package noctiluca.timeline.domain.usecase.internal

import noctiluca.account.infra.repository.AccountRepository
import noctiluca.timeline.domain.usecase.FetchCurrentAuthorizedAccountUseCase

internal class FetchCurrentAuthorizedAccountUseCaseImpl(
    private val repository: AccountRepository,
) : FetchCurrentAuthorizedAccountUseCase {
    override suspend fun execute() = repository.fetchCurrentAccount()
}
