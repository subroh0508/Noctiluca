package noctiluca.network.mastodon

interface AuthorizationTokenProvider {
    suspend fun getCurrentAccessToken(): String?
    suspend fun getCurrentDomain(): String?
}
