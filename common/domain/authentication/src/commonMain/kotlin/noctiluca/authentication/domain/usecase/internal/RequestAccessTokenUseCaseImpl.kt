package noctiluca.authentication.domain.usecase.internal

import noctiluca.authentication.domain.usecase.RequestAccessTokenUseCase
import noctiluca.authentication.infra.repository.TokenRepository
import noctiluca.model.Uri

internal class RequestAccessTokenUseCaseImpl(
    private val repository: TokenRepository,
) : RequestAccessTokenUseCase {
    override suspend fun execute(
        code: String,
        redirectUri: Uri,
    ) = repository.fetchToken(code, redirectUri)
}
