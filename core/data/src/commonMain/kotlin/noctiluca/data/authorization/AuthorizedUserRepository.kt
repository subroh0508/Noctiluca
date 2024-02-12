package noctiluca.data.authorization

import kotlinx.coroutines.flow.Flow
import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser

interface AuthorizedUserRepository {
    fun currentAuthorizedUser(): Flow<AuthorizedUser?>

    suspend fun switchCurrent(id: AccountId)
    suspend fun expireCurrent()
}
