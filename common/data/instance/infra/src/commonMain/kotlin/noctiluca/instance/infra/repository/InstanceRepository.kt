package noctiluca.instance.infra.repository

interface InstanceRepository {
    suspend fun search(query: String): List<String>
}