package noctiluca.data.accountdetail

import kotlinx.coroutines.flow.Flow
import noctiluca.model.AccountId
import noctiluca.model.accountdetail.StatusesQuery
import noctiluca.model.status.Status

interface AccountStatusRepository {
    fun statuses(id: AccountId): Flow<Map<StatusesQuery, List<Status>>>

    suspend fun loadStatuses(
        id: AccountId,
        query: StatusesQuery,
    )
}
