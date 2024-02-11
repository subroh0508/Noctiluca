package noctiluca.data.authentication

interface AuthorizedUserRepository {
    suspend fun expireCurrent()
}
