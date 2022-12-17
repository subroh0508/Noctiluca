package noctiluca.authentication.domain.usecase.internal

import noctiluca.authentication.domain.usecase.SearchMastodonInstancesUseCase

internal class SearchMastodonInstancesUseCaseImpl(

) : SearchMastodonInstancesUseCase {
    override suspend fun execute(query: String): List<String> {
        return listOf()
    }
}