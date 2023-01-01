package noctiluca.authentication.domain.di

import noctiluca.authentication.domain.usecase.RequestAccessTokenUseCase
import noctiluca.authentication.domain.usecase.RequestAppCredentialUseCase
import noctiluca.authentication.domain.usecase.SearchMastodonInstancesUseCase
import noctiluca.authentication.domain.usecase.ShowMastodonInstanceUseCase
import noctiluca.authentication.domain.usecase.internal.RequestAccessTokenUseCaseImpl
import noctiluca.authentication.domain.usecase.internal.RequestAppCredentialUseCaseImpl
import noctiluca.authentication.domain.usecase.internal.SearchMastodonInstancesUseCaseImpl
import noctiluca.authentication.domain.usecase.internal.ShowMastodonInstanceUseCaseImpl
import org.koin.dsl.ScopeDSL

@Suppress("FunctionName")
fun ScopeDSL.AuthenticationDomainModule() {
    scoped<SearchMastodonInstancesUseCase> { SearchMastodonInstancesUseCaseImpl(get()) }
    scoped<ShowMastodonInstanceUseCase> { ShowMastodonInstanceUseCaseImpl(get()) }
    scoped<RequestAppCredentialUseCase> { RequestAppCredentialUseCaseImpl(get()) }
    scoped<RequestAccessTokenUseCase> { RequestAccessTokenUseCaseImpl(get()) }
}
