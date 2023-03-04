package noctiluca.api.token

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.first
import noctiluca.api.token.internal.Token
import noctiluca.api.token.internal.find
import noctiluca.api.token.internal.hasSameIdentifier
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

    actual suspend fun setCurrent(id: AccountId, domain: Domain): AuthorizedUser {
        dataStore.updateData {
            val token = it.find(id, domain)

            listOfNotNull(token?.copy(current = true)) + it.mapNotNull { t ->
                if (t.hasSameIdentifier(id, domain)) {
                    return@mapNotNull null
                }

                t.copy(current = false)
            }
        }

        return Token(dataStore.data.first().first(Token.Json::current))
    }

    actual suspend fun getAccessToken(id: AccountId, domain: Domain) =
        dataStore.data.first().find(id, domain)?.accessToken

    actual suspend fun add(
        id: AccountId,
        domain: Domain,
        accessToken: String,
    ): List<AuthorizedUser> = dataStore.updateData {
        it + Token.Json(id, domain, accessToken)
    }.map(::Token)

    actual suspend fun delete(id: AccountId, domain: Domain): List<AuthorizedUser> =
        dataStore.updateData {
            it.filterNot { t -> t.hasSameIdentifier(id, domain) }
        }.map(::Token)
}
