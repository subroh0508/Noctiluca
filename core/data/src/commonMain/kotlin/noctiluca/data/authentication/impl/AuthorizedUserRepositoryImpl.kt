package noctiluca.data.authentication.impl

import noctiluca.data.authentication.AuthorizedUserRepository
import noctiluca.datastore.AuthenticationTokenDataStore

internal class AuthorizedUserRepositoryImpl(
    private val dataStore: AuthenticationTokenDataStore,
) : AuthorizedUserRepository {
    override suspend fun expireCurrent() {
        dataStore.getCurrent()?.let { dataStore.delete(it.id) }
        dataStore.getAll().firstOrNull()?.let {
            dataStore.setCurrent(it.id)
        }
    }
}
