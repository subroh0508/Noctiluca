package noctiluca.accountdetail.infra.repository

import noctiluca.accountdetail.model.AccountDetail
import noctiluca.accountdetail.model.Relationship
import noctiluca.accountdetail.model.Relationships
import noctiluca.model.AccountId

interface AccountDetailRepository {
    suspend fun fetch(id: AccountId): AccountDetail
    suspend fun fetchRelationships(id: AccountId): Relationships
}
