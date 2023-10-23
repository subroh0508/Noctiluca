package noctiluca.accountdetail.domain.usecase.internal

import noctiluca.accountdetail.domain.model.StatusesQuery
import noctiluca.accountdetail.domain.usecase.FetchAccountStatusesUseCase
import noctiluca.data.accountdetail.AccountDetailRepository
import noctiluca.model.AccountId

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