package noctiluca.authentication.domain.usecase

interface SearchMastodonInstancesUseCase {
    suspend fun execute(query: String): List<String>
}