package noctiluca.data.accountdetail

import kotlinx.coroutines.flow.Flow
import noctiluca.model.AccountId
import noctiluca.model.StatusId
import noctiluca.model.accountdetail.AccountAttributes
import noctiluca.model.status.Status

interface AccountDetailRepository {
    fun attributes(id: AccountId): Flow<AccountAttributes>

    suspend fun fetchStatuses(
        id: AccountId,
        maxId: StatusId? = null,
        onlyMedia: Boolean = false,
        excludeReplies: Boolean = true,
    ): List<Status>
}
