package noctiluca.authentication.domain.usecase.internal

import noctiluca.authentication.domain.usecase.RequestAppCredentialUseCase
import noctiluca.authentication.infra.repository.TokenRepository
import noctiluca.model.Hostname
import noctiluca.model.Uri

internal class RequestAppCredentialUseCaseImpl(
    private val repository: TokenRepository,
) : RequestAppCredentialUseCase {
    override suspend fun execute(
        hostname: Hostname,
        clientName: String,
        redirectUri: Uri,
    ): Uri {
        val credential = repository.fetchAppCredential(hostname, clientName, redirectUri)

        repository.cacheAppCredential(credential)

        return credential.authorizeUrl
    }
}
