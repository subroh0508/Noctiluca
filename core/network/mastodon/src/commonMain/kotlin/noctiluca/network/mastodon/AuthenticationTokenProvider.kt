package noctiluca.network.mastodon

interface AuthenticationTokenProvider {
    suspend fun getCurrentAccessToken(): String?
    suspend fun getCurrentDomain(): String?
}
