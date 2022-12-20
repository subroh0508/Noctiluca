package noctiluca.api.token

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser
import java.util.prefs.Preferences

@Suppress("UNCHECKED_CAST")
actual class LocalTokenCache internal constructor(
    private val prefs: Preferences,
) : Token.Cache {
    companion object {
        private const val TOKEN_LIST = "TOKEN_LIST"
    }

    override suspend fun getCurrentAccessToken() = (getCurrent() as? Token)?.accessToken

    override suspend fun getCurrentDomain() = (getCurrent() as? Token)?.hostname

    actual suspend fun getAll(): List<AuthorizedUser> = withContext(Dispatchers.IO) {
        getTokensJson().map(::Token)
    }

    actual suspend fun getCurrent(): AuthorizedUser? = withContext(Dispatchers.IO) {
        getTokensJson().find(Token.Json::current)?.let(::Token)
    }
    actual suspend fun setCurrent(id: AccountId): AuthorizedUser = withContext(Dispatchers.IO) {
        val prevTokens = getTokensJson()

        val token = prevTokens.find { it.accountId == id.value }
        val nextTokens = listOfNotNull(token?.copy(current = true)) + prevTokens.mapNotNull {
            if (it.accountId == id.value) {
                return@mapNotNull null
            }

            it.copy(current = false)
        }

        saveTokensJson(nextTokens)

        Token(getTokensJson().first(Token.Json::current))
    }

    actual suspend fun add(token: Token) = withContext(Dispatchers.IO) {
        val nextTokens = getTokensJson() + listOf(Token.Json(token))
        saveTokensJson(nextTokens)

        nextTokens as List<AuthorizedUser>
    }

    actual suspend fun delete(id: AccountId) = withContext(Dispatchers.IO) {
        val nextTokens = getTokensJson().filterNot { it.accountId == id.value }
        saveTokensJson(nextTokens)

        nextTokens as List<AuthorizedUser>
    }

    private fun getTokensJson() = prefs[TOKEN_LIST, null]?.let {
        Json.decodeFromString<Array<Token.Json>>(it)
    }?.toList() ?: listOf()

    private fun saveTokensJson(tokens: List<Token.Json>) {
        prefs.put(TOKEN_LIST, Json.encodeToString(tokens.toTypedArray()))
    }
}
