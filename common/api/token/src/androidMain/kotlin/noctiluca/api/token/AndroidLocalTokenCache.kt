package noctiluca.api.token

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.first
import noctiluca.api.token.internal.Token
import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser
import noctiluca.model.Domain
import noctiluca.repository.TokenCache

actual class LocalTokenCache internal constructor(
    private val dataStore: DataStore<List<Token.Json>>,
) : TokenCache {
    override suspend fun getCurrentAccessToken() = (getCurrent() as? Token)?.accessToken

    override suspend fun getCurrentDomain() = (getCurrent() as? Token)?.domain?.value

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

    actual suspend fun add(
        id: AccountId,
        domain: Domain,
        accessToken: String,
    ): List<AuthorizedUser> = dataStore.updateData {
        it + Token.Json(id, domain, accessToken)
    }.map(::Token)

    actual suspend fun delete(id: AccountId): List<AuthorizedUser> = dataStore.updateData {
        it.filterNot { t -> t.accountId == id.value }
    }.map(::Token)
}
