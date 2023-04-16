package noctiluca.accountdetail.domain.usecase

import noctiluca.model.AccountId
import noctiluca.model.StatusId
import noctiluca.status.model.Status

interface FetchAccountStatusesUseCase {
    suspend fun execute(
        id: AccountId,
        maxId: StatusId? = null,
    ): List<Status>
}
