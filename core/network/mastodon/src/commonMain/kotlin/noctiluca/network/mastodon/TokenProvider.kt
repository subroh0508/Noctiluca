package noctiluca.network.mastodon

interface TokenProvider {
    suspend fun getCurrentAccessToken(): String?
    suspend fun getCurrentDomain(): String?
}
