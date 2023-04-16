package noctiluca.accountdetail.domain.usecase.internal

import noctiluca.accountdetail.domain.model.StatusesQuery
import noctiluca.accountdetail.domain.usecase.FetchAccountStatusesUseCase
import noctiluca.accountdetail.infra.repository.AccountDetailRepository
import noctiluca.model.AccountId
import noctiluca.model.StatusId

internal class FetchAccountStatusesUseCaseImpl(
    private val repository: AccountDetailRepository,
) : FetchAccountStatusesUseCase {
    override suspend fun execute(
        id: AccountId,
        query: StatusesQuery,
    ) = repository.fetchStatuses(
        id,
        query.maxId,
        query is StatusesQuery.OnlyMedia,
        query !is StatusesQuery.WithReplies,
    )
}
