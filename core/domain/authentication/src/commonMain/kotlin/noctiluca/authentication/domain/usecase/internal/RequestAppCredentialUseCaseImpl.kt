package noctiluca.authentication.domain.usecase.internal

import noctiluca.data.authentication.AppCredentialRepository
import noctiluca.authentication.domain.usecase.RequestAppCredentialUseCase
import noctiluca.model.Domain
import noctiluca.model.Uri

internal class RequestAppCredentialUseCaseImpl(
    private val repository: AppCredentialRepository,
) : RequestAppCredentialUseCase {
    override suspend fun execute(
        domain: Domain,
        clientName: String,
        redirectUri: Uri,
    ) = repository.fetchAppCredential(
        domain,
        clientName,
        redirectUri,
    ).authorizeUrl
}
