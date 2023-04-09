package noctiluca.accountdetail.domain.usecase.internal

import noctiluca.accountdetail.domain.usecase.FetchAccountDetailUseCase
import noctiluca.accountdetail.infra.repository.AccountDetailRepository
import noctiluca.model.AccountId

internal class FetchAccountDetailUseCaseImpl(
    private val repository: AccountDetailRepository,
) : FetchAccountDetailUseCase {
    override suspend fun execute(id: AccountId) = repository.fetch(id)
}
