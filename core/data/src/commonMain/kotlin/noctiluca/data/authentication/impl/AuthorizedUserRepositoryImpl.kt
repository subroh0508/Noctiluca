package noctiluca.data.authentication.impl

import noctiluca.data.authentication.AuthorizedUserRepository
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
