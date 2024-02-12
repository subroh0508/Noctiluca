package noctiluca.data.authorization

import kotlinx.coroutines.flow.Flow
import noctiluca.model.AuthorizedUser

interface AuthorizedUserRepository {
    fun currentAuthorizedUser(): Flow<AuthorizedUser?>

    suspend fun expireCurrent()
}
