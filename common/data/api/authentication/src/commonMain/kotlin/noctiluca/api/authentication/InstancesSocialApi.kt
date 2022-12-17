package noctiluca.api.authentication

interface InstancesSocialApi {
    suspend fun search(
        query: String,
        count: Int = 20,
    ): String
}