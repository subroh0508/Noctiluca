package noctiluca.timeline.domain.usecase.internal

import kotlinx.coroutines.flow.combine
import noctiluca.data.account.AuthorizedAccountRepository
import noctiluca.timeline.domain.model.CurrentAuthorizedAccount
import noctiluca.timeline.domain.usecase.FetchCurrentAuthorizedAccountUseCase

internal class FetchCurrentAuthorizedAccountUseCaseImpl(
    private val repository: AuthorizedAccountRepository,
) : FetchCurrentAuthorizedAccountUseCase {
    override suspend fun execute() = combine(
        repository.current(),
        repository.others(),
    ) { current, others -> CurrentAuthorizedAccount(current, others) }
}
