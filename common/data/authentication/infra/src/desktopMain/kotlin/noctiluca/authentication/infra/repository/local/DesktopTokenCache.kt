package noctiluca.authentication.infra.repository.local

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import noctiluca.authentication.infra.internal.CachedToken
import noctiluca.authentication.infra.internal.Token
import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser
import java.util.prefs.Preferences

@Suppress("UNCHECKED_CAST")
internal actual class TokenCache(
    private val prefs: Preferences,
) {
    companion object {
        private const val TOKEN_LIST = "TOKEN_LIST"
    }

    actual suspend fun getAll(): List<AuthorizedUser> = withContext(Dispatchers.IO) {
        getCachedTokens().map(::Token)
    }

    actual suspend fun getCurrent(): AuthorizedUser? = withContext(Dispatchers.IO) {
        getCachedTokens().find(CachedToken::current)?.let(::Token)
    }
    actual suspend fun setCurrent(id: AccountId): AuthorizedUser = withContext(Dispatchers.IO) {
        val prevTokens = getCachedTokens()

        val token = prevTokens.find { it.accountId == id.value }
        val nextTokens = listOfNotNull(token?.copy(current = true)) + prevTokens.mapNotNull {
            if (it.accountId == id.value) {
                return@mapNotNull null
            }

            it.copy(current = false)
        }

        saveCachedTokens(nextTokens)

        Token(getCachedTokens().first(CachedToken::current))
    }

    actual suspend fun add(token: Token) = withContext(Dispatchers.IO) {
        val nextTokens = getCachedTokens() + listOf(CachedToken(token))
        saveCachedTokens(nextTokens)

        nextTokens as List<AuthorizedUser>
    }

    actual suspend fun delete(id: AccountId) = withContext(Dispatchers.IO) {
        val nextTokens = getCachedTokens().filterNot { it.accountId == id.value }
        saveCachedTokens(nextTokens)

        nextTokens as List<AuthorizedUser>
    }

    private fun getCachedTokens() = prefs[TOKEN_LIST, null]?.let {
        Json.decodeFromString<Array<CachedToken>>(it)
    }?.toList() ?: listOf()

    private fun saveCachedTokens(tokens: List<CachedToken>) {
        prefs.put(TOKEN_LIST, Json.encodeToString(tokens.toTypedArray()))
    }
}
