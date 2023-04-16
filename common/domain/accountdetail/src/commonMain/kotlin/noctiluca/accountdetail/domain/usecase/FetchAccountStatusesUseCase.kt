package noctiluca.accountdetail.domain.usecase

import noctiluca.accountdetail.domain.model.StatusesQuery
import noctiluca.model.AccountId
import noctiluca.status.model.Status

interface FetchAccountStatusesUseCase {
    suspend fun execute(
        id: AccountId,
        query: StatusesQuery,
    ): List<Status>
}
