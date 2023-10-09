package noctiluca.accountdetail.infra.repository

import noctiluca.accountdetail.model.AccountAttributes
import noctiluca.accountdetail.model.Relationships
import noctiluca.model.AccountId
import noctiluca.model.StatusId
import noctiluca.status.model.Status

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
