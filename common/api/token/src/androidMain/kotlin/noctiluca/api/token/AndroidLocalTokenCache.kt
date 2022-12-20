package noctiluca.api.token

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.first
import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser

actual class LocalTokenCache internal constructor(
    private val dataStore: DataStore<List<Token.Json>>,
) : Token.Cache {
    override suspend fun getCurrentAccessToken() = (getCurrent() as? Token)?.accessToken

    actual suspend fun getAll(): List<AuthorizedUser> = dataStore.data.first().map(::Token)

    actual suspend fun getCurrent(): AuthorizedUser? = dataStore.data.first().find(Token.Json::current)?.let(::Token)

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

        return Token(dataStore.data.first().first(Token.Json::current))
    }

    actual suspend fun add(token: Token): List<AuthorizedUser> = dataStore.updateData {
        it + Token.Json(token)
    }.map(::Token)

    actual suspend fun delete(id: AccountId): List<AuthorizedUser> = dataStore.updateData {
        it.filterNot { t -> t.accountId == id.value }
    }.map(::Token)
}
