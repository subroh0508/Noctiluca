package noctiluca.authentication.domain.usecase

import noctiluca.model.authentication.Instance

interface FetchMastodonInstanceUseCase {
    suspend fun execute(domain: String): Instance
}
