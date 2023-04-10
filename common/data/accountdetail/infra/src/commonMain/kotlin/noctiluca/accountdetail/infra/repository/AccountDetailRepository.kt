package noctiluca.accountdetail.infra.repository

import noctiluca.accountdetail.model.AccountAttributes
import noctiluca.accountdetail.model.Relationships
import noctiluca.model.AccountId

interface AccountDetailRepository {
    suspend fun fetch(id: AccountId): AccountAttributes
    suspend fun fetchRelationships(id: AccountId): Relationships
}
