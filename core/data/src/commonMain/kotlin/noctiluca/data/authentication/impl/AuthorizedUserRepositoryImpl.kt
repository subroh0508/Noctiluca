package noctiluca.data.authentication.impl

import noctiluca.data.authentication.AuthorizedUserRepository
import noctiluca.datastore.AuthenticationTokenDataStore

internal class AuthorizedUserRepositoryImpl(
    private val authenticationTokenDataStore: AuthenticationTokenDataStore,
) : AuthorizedUserRepository {
    override suspend fun expireCurrent() {
        authenticationTokenDataStore.getCurrent()?.let { authenticationTokenDataStore.delete(it.id) }
        authenticationTokenDataStore.getAll().firstOrNull()?.let {
            authenticationTokenDataStore.setCurrent(it.id)
        }
    }
}
