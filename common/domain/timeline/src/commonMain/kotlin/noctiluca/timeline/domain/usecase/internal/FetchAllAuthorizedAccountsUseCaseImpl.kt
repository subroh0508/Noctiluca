package noctiluca.timeline.domain.usecase.internal

import noctiluca.account.infra.repository.AccountRepository
import noctiluca.timeline.domain.usecase.FetchAllAuthorizedAccountsUseCase

internal class FetchAllAuthorizedAccountsUseCaseImpl(
    private val repository: AccountRepository,
) : FetchAllAuthorizedAccountsUseCase {
    override suspend fun execute() = repository.fetchAllAuthorizedAccounts()
}