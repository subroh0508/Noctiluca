package noctiluca.data.authorization.impl

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import noctiluca.data.authorization.AuthorizedUserRepository
import noctiluca.datastore.AuthorizationTokenDataStore
import noctiluca.model.AuthorizedTokenNotFoundException
import noctiluca.model.AuthorizedUser
import noctiluca.model.HttpUnauthorizedException
import noctiluca.network.mastodon.MastodonApiV1

internal class AuthorizedUserRepositoryImpl(
    private val dataStore: AuthorizationTokenDataStore,
    private val v1: MastodonApiV1,
) : AuthorizedUserRepository {
    private val currentAuthorizedUserStateFlow by lazy { MutableStateFlow<AuthorizedUser?>(null) }

    override fun currentAuthorizedUser() = currentAuthorizedUserStateFlow
        .onStart { currentAuthorizedUserStateFlow.value = fetchCurrent() }
        .catch { e ->
            if (e is HttpUnauthorizedException) {
                expireCurrent()
            }
        }

    override suspend fun expireCurrent() {
        dataStore.getCurrent()?.let { dataStore.delete(it.id) }
        dataStore.getAll().firstOrNull()?.let {
            dataStore.setCurrent(it.id)
        }

        currentAuthorizedUserStateFlow.value = fetchCurrent()
    }

    private suspend fun fetchCurrent() = dataStore.getCurrent()?.also {
        v1.getVerifyAccountsCredentials(it.domain.value)
    } ?: throw AuthorizedTokenNotFoundException
}
