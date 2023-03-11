package noctiluca.timeline.domain.usecase.internal

import kotlinx.coroutines.flow.flow
import noctiluca.account.infra.repository.AuthorizedAccountRepository
import noctiluca.timeline.domain.usecase.FetchAllAuthorizedAccountsUseCase

internal class FetchAllAuthorizedAccountsUseCaseImpl(
    private val repository: AuthorizedAccountRepository,
) : FetchAllAuthorizedAccountsUseCase {
    override suspend fun execute() = flow {
        repository.getAll().drop(1).forEach { cache ->
            emit(cache)

            /*
            runCatching { repository.refresh(cache.id) }
                .onSuccess { emit(it) }
            */
        }
    }
}
