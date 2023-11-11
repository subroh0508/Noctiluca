package noctiluca.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import noctiluca.datastore.internal.Token
import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser
import noctiluca.model.Domain

internal class AndroidAuthenticationTokenDataStore private constructor(
    private val dataStore: DataStore<List<Token.Json>>,
) : AuthenticationTokenDataStore {
    internal constructor(context: Context, json: Json) : this(
        context.getJsonDataStore(
            JsonSerializer(json, listOf()),
            AuthenticationTokenDataStore::class.simpleName ?: "",
        )
    )

    override suspend fun getCurrentAccessToken() = (getCurrent() as? Token)?.accessToken

    override suspend fun getCurrentDomain() = (getCurrent() as? Token)?.domain?.value

    override suspend fun getAll(): List<AuthorizedUser> {
        val current = getCurrent()

        return listOfNotNull(current) + dataStore.data.first().filterNot {
            current != null && it.accountId == current.id.value
        }.map(::Token)
    }

    override suspend fun getCurrent(): AuthorizedUser? =
        dataStore.data.first().find(Token.Json::current)?.let(::Token)

    override suspend fun setCurrent(id: AccountId): AuthorizedUser {
        dataStore.updateData { json ->
            val token = json.find { it.accountId == id.value }

            listOfNotNull(token?.copy(current = true)) + json.mapNotNull { t ->
                if (t.accountId == id.value) {
                    return@mapNotNull null
                }

                t.copy(current = false)
            }
        }

        return Token(dataStore.data.first().first(Token.Json::current))
    }

    override suspend fun getAccessToken(id: AccountId) =
        dataStore.data.first().find { it.accountId == id.value }?.accessToken

    override suspend fun getDomain(id: AccountId) =
        dataStore.data.first().find { it.accountId == id.value }?.domain?.let(::Domain)

    override suspend fun add(
        id: AccountId,
        domain: Domain,
        accessToken: String,
    ): List<AuthorizedUser> = dataStore.updateData {
        it + Token.Json(id, domain, accessToken)
    }.map(::Token)

    override suspend fun delete(id: AccountId): List<AuthorizedUser> =
        dataStore.updateData {
            it.filterNot { t -> t.accountId == id.value }
        }.map(::Token)
}
