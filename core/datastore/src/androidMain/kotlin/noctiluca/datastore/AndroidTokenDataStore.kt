package noctiluca.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import noctiluca.datastore.JsonSerializer
import noctiluca.datastore.getJsonDataStore
import noctiluca.datastore.internal.Token
import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser
import noctiluca.model.Domain

actual class TokenDataStore internal constructor(
    private val dataStore: DataStore<List<Token.Json>>,
) {
    internal constructor(context: Context, json: Json) : this(
        context.getJsonDataStore(
            JsonSerializer(json, listOf()),
            TokenDataStore::class.simpleName ?: "",
        )
    )

    actual suspend fun getCurrentAccessToken() = (getCurrent() as? Token)?.accessToken

    actual suspend fun getCurrentDomain() = (getCurrent() as? Token)?.domain?.value

    actual suspend fun getAll(): List<AuthorizedUser> {
        val current = getCurrent()

        return listOfNotNull(current) + dataStore.data.first().filterNot {
            current != null && it.accountId == current.id.value
        }.map(::Token)
    }

    actual suspend fun getCurrent(): AuthorizedUser? =
        dataStore.data.first().find(Token.Json::current)?.let(::Token)

    actual suspend fun setCurrent(id: AccountId): AuthorizedUser {
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

    actual suspend fun getAccessToken(id: AccountId) =
        dataStore.data.first().find { it.accountId == id.value }?.accessToken

    actual suspend fun getDomain(id: AccountId) =
        dataStore.data.first().find { it.accountId == id.value }?.domain?.let(::Domain)

    actual suspend fun add(
        id: AccountId,
        domain: Domain,
        accessToken: String,
    ): List<AuthorizedUser> = dataStore.updateData {
        it + Token.Json(id, domain, accessToken)
    }.map(::Token)

    actual suspend fun delete(id: AccountId): List<AuthorizedUser> =
        dataStore.updateData {
            it.filterNot { t -> t.accountId == id.value }
        }.map(::Token)
}
