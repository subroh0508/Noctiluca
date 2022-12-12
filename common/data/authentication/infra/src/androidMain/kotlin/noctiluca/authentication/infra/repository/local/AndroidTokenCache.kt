package noctiluca.authentication.infra.repository.local

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.first
import noctiluca.authentication.infra.internal.CachedToken
import noctiluca.authentication.infra.internal.Token
import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser

@Suppress("UNCHECKED_CAST")
internal actual class TokenCache(
    private val dataStore: DataStore<List<CachedToken>>,
) {
    actual suspend fun getAll() = dataStore.data.first() as List<AuthorizedUser>

    actual suspend fun getCurrent(): AuthorizedUser? = dataStore.data.first().find(CachedToken::current)?.let(::Token)

    actual suspend fun setCurrent(id: AccountId): AuthorizedUser {
        dataStore.updateData {
            val token = it.find { t -> t.accountId == id.value }

            listOfNotNull(token?.copy(current = true)) + it.mapNotNull { t ->
                if (t.accountId == id.value) {
                    return@mapNotNull null
                }

                t.copy(current = false)
            }
        }

        return Token(dataStore.data.first().first(CachedToken::current))
    }

    actual suspend fun add(token: Token) = dataStore.updateData {
        it + CachedToken(token)
    } as List<AuthorizedUser>

    actual suspend fun delete(id: AccountId) = dataStore.updateData {
        it.filterNot { t -> t.accountId == id.value }
    } as List<AuthorizedUser>
}
