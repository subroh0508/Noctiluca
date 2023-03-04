package noctiluca.timeline.domain.usecase.internal

import kotlinx.coroutines.flow.flow
import noctiluca.account.infra.repository.AuthorizedAccountRepository
import noctiluca.timeline.domain.usecase.FetchCurrentAuthorizedAccountUseCase

internal class FetchCurrentAuthorizedAccountUseCaseImpl(
    private val repository: AuthorizedAccountRepository,
) : FetchCurrentAuthorizedAccountUseCase {
    override suspend fun execute() = flow {
        val cache = repository.getCurrent()
        if (cache == null) {
            emit(repository.fetchCurrent())
            return@flow
        }

        val (account, domain) = cache
        emit(cache)
        emit(repository.refresh(account.id) to domain)
    }
}
