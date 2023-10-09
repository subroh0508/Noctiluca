package noctiluca.api.mastodon

interface TokenProvider {
    suspend fun getCurrentAccessToken(): String?
    suspend fun getCurrentDomain(): String?
}
