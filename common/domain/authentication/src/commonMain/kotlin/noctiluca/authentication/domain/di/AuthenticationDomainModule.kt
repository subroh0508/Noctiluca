package noctiluca.authentication.domain.di

import noctiluca.authentication.domain.usecase.*
import noctiluca.authentication.domain.usecase.internal.*
import noctiluca.authentication.domain.usecase.internal.RequestAccessTokenUseCaseImpl
import noctiluca.authentication.domain.usecase.internal.RequestAppCredentialUseCaseImpl
import noctiluca.authentication.domain.usecase.internal.SearchMastodonInstancesUseCaseImpl
import noctiluca.authentication.domain.usecase.internal.ShowMastodonInstanceUseCaseImpl
import org.koin.dsl.ScopeDSL

@Suppress("FunctionName")
fun ScopeDSL.AuthenticationDomainModule() {
    scoped<SearchMastodonInstancesUseCase> { SearchMastodonInstancesUseCaseImpl(get()) }
    scoped<ShowMastodonInstanceUseCase> { ShowMastodonInstanceUseCaseImpl(get()) }
    scoped<FetchLocalTimelineUseCase> { FetchLocalTimelineUseCaseImpl(get()) }
    scoped<RequestAppCredentialUseCase> { RequestAppCredentialUseCaseImpl(get()) }
    scoped<RequestAccessTokenUseCase> { RequestAccessTokenUseCaseImpl(get()) }
}
