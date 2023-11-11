package noctiluca.datastore

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import noctiluca.datastore.internal.Token
import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser
import noctiluca.model.Domain

@Suppress("UNCHECKED_CAST")
internal class DesktopAuthenticationTokenDataStore(
    private val prefs: JsonPreferences<List<Token.Json>>,
) : AuthenticationTokenDataStore {
    override suspend fun getCurrentAccessToken() = (getCurrent() as? Token)?.accessToken

    override suspend fun getCurrentDomain() = (getCurrent() as? Token)?.domain?.value

    override suspend fun getAll(): List<AuthorizedUser> = withContext(Dispatchers.IO) {
        val current = getCurrent()

        listOfNotNull(current) + prefs.data.filterNot {
            current != null && it.accountId == current.id.value
        }.map(::Token)
    }

    override suspend fun getCurrent(): AuthorizedUser? = withContext(Dispatchers.IO) {
        prefs.data.find(Token.Json::current)?.let(::Token)
    }

    override suspend fun setCurrent(id: AccountId): AuthorizedUser =
        withContext(Dispatchers.IO) {
            val prevTokens = prefs.data

            val token = prevTokens.find { it.accountId == id.value }
            val nextTokens = listOfNotNull(token?.copy(current = true)) + prevTokens.mapNotNull {
                if (it.accountId == id.value) {
                    return@mapNotNull null
                }

                it.copy(current = false)
            }

            prefs.save(nextTokens)

            Token(prefs.data.first(Token.Json::current))
        }

    override suspend fun getAccessToken(id: AccountId) = withContext(Dispatchers.IO) {
        prefs.data.find { it.accountId == id.value }?.accessToken
    }

    override suspend fun getDomain(id: AccountId) = withContext(Dispatchers.IO) {
        prefs.data.find { it.accountId == id.value }?.domain?.let(::Domain)
    }

    override suspend fun add(
        id: AccountId,
        domain: Domain,
        accessToken: String,
    ) = withContext(Dispatchers.IO) {
        val nextTokens = prefs.data + listOf(Token.Json(id, domain, accessToken))
        prefs.save(nextTokens)

        nextTokens as List<AuthorizedUser>
    }

    override suspend fun delete(id: AccountId) = withContext(Dispatchers.IO) {
        val nextTokens = prefs.data.filterNot { it.accountId == id.value }
        prefs.save(nextTokens)

        nextTokens as List<AuthorizedUser>
    }
}
