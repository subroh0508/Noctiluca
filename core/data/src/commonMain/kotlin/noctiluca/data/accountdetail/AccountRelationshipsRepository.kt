package noctiluca.data.accountdetail

import kotlinx.coroutines.flow.Flow
import noctiluca.model.AccountId
import noctiluca.model.accountdetail.Relationships

interface AccountRelationshipsRepository {
    fun relationships(id: AccountId): Flow<Relationships>

    suspend fun follow(id: AccountId)
    suspend fun block(id: AccountId)
    suspend fun mute(id: AccountId)

    suspend fun toggleReblog(id: AccountId)
    suspend fun toggleNotify(id: AccountId)
}
