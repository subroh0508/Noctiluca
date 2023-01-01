package noctiluca.authentication.domain.usecase.internal

import noctiluca.authentication.domain.usecase.RequestAccessTokenUseCase
import noctiluca.authentication.infra.repository.TokenRepository
import noctiluca.model.Hostname
import noctiluca.model.Uri

internal class RequestAccessTokenUseCaseImpl(
    private val repository: TokenRepository,
) : RequestAccessTokenUseCase {
    override suspend fun execute(
        hostname: Hostname,
        redirectUri: Uri,
        code: String
    ) = repository.fetchToken(code, redirectUri)
}
