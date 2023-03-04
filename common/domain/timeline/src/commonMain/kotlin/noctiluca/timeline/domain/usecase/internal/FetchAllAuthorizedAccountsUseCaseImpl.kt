package noctiluca.timeline.domain.usecase.internal

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.mapNotNull
import noctiluca.account.infra.repository.AccountRepository
import noctiluca.repository.TokenProvider
import noctiluca.timeline.domain.usecase.FetchAllAuthorizedAccountsUseCase

internal class FetchAllAuthorizedAccountsUseCaseImpl(
    private val repository: AccountRepository,
    private val tokenProvider: TokenProvider,
) : FetchAllAuthorizedAccountsUseCase {
    override suspend fun execute() = tokenProvider.getAuthorizedUsers(includeCurrent = false)
        .asFlow()
        .mapNotNull {
            runCatching { repository.fetchAuthorizedAccount(it) }
                .getOrNull()
        }
}
