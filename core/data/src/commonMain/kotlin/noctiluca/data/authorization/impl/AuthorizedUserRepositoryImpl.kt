package noctiluca.data.authorization.impl

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onStart
import noctiluca.data.authorization.AuthorizedUserRepository
import noctiluca.datastore.AuthorizationTokenDataStore
import noctiluca.model.AccountId
import noctiluca.model.AuthorizedTokenNotFoundException
import noctiluca.model.AuthorizedUser
import noctiluca.network.mastodon.MastodonApiV1

internal class AuthorizedUserRepositoryImpl(
    private val v1: MastodonApiV1,
    private val dataStore: AuthorizationTokenDataStore,
) : AuthorizedUserRepository {
    private val currentAuthorizedUserStateFlow by lazy { MutableStateFlow<AuthorizedUser?>(null) }

    override fun currentAuthorizedUser() = currentAuthorizedUserStateFlow
        .onStart {
            if (currentAuthorizedUserStateFlow.value != null) {
                return@onStart
            }

            currentAuthorizedUserStateFlow.value = fetchCurrent()
        }

    override suspend fun switchCurrent(id: AccountId) {
        dataStore.setCurrent(id)
        currentAuthorizedUserStateFlow.value = fetchCurrent()
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
