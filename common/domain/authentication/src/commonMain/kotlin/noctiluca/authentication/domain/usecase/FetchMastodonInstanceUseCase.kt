package noctiluca.authentication.domain.usecase

import noctiluca.instance.model.Instance

interface FetchMastodonInstanceUseCase {
    suspend fun execute(domain: String): Instance
}
