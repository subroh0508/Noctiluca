package noctiluca.authentication.domain.usecase

import noctiluca.model.authentication.Instance

interface SearchMastodonInstancesUseCase {
    suspend fun execute(query: String): List<Instance.Suggest>
}
