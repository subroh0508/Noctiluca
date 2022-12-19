package noctiluca.authentication.domain.usecase

import noctiluca.instance.model.Instance

interface SearchMastodonInstancesUseCase {
    suspend fun execute(query: String): List<Instance>
}