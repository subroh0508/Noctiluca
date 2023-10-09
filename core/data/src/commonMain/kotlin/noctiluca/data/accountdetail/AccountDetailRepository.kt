package noctiluca.data.accountdetail

import noctiluca.model.AccountId
import noctiluca.model.StatusId
import noctiluca.model.accountdetail.AccountAttributes
import noctiluca.model.accountdetail.Relationships
import noctiluca.model.status.Status

interface AccountDetailRepository {
    suspend fun fetch(id: AccountId): AccountAttributes
    suspend fun fetchRelationships(id: AccountId): Relationships
    suspend fun fetchStatuses(
        id: AccountId,
        maxId: StatusId? = null,
        onlyMedia: Boolean = false,
        excludeReplies: Boolean = true,
    ): List<Status>
}
