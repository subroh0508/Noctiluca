package noctiluca.data.authorization.impl

import noctiluca.data.authorization.AuthorizedUserRepository
import noctiluca.datastore.AuthorizationTokenDataStore

internal class AuthorizedUserRepositoryImpl(
    private val dataStore: AuthorizationTokenDataStore,
) : AuthorizedUserRepository {
    override suspend fun expireCurrent() {
        dataStore.getCurrent()?.let { dataStore.delete(it.id) }
        dataStore.getAll().firstOrNull()?.let {
            dataStore.setCurrent(it.id)
        }
    }
}
