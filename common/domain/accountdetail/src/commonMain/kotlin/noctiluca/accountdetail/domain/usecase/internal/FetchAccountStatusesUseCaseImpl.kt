package noctiluca.accountdetail.domain.usecase.internal

import noctiluca.accountdetail.domain.usecase.FetchAccountStatusesUseCase
import noctiluca.accountdetail.infra.repository.AccountDetailRepository
import noctiluca.model.AccountId
import noctiluca.model.StatusId

internal class FetchAccountStatusesUseCaseImpl(
    private val repository: AccountDetailRepository,
) : FetchAccountStatusesUseCase {
    override suspend fun execute(
        id: AccountId,
        maxId: StatusId?,
    ) = repository.fetchStatuses(id, maxId)
}
