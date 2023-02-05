package noctiluca.authentication.domain.usecase

import noctiluca.instance.model.Instance

interface ShowMastodonInstanceUseCase {
    suspend fun execute(domain: String): Instance
}
