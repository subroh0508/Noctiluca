package noctiluca.api.instancessocial

interface InstancesSocialApi {
    suspend fun search(
        query: String,
        count: Int = 20,
    ): String
}