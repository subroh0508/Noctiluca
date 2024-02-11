package noctiluca.authentication.domain.usecase.internal

import noctiluca.authentication.domain.usecase.RequestAppCredentialUseCase
import noctiluca.data.authentication.AuthenticationRepository
import noctiluca.model.Domain
import noctiluca.model.Uri

internal class RequestAppCredentialUseCaseImpl(
    private val repository: AuthenticationRepository,
) : RequestAppCredentialUseCase {
    override suspend fun execute(
        domain: Domain,
        clientName: String,
        redirectUri: Uri,
    ) = repository.fetchAuthorizeUrl(
        domain,
        clientName,
        redirectUri,
    )
}
