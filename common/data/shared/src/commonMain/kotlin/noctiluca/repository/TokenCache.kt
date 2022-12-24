package noctiluca.repository

interface TokenCache {
    suspend fun getCurrentAccessToken(): String?
    suspend fun getCurrentDomain(): String?
}