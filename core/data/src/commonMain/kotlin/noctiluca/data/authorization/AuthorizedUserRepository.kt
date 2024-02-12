package noctiluca.data.authorization

interface AuthorizedUserRepository {
    suspend fun expireCurrent()
}
