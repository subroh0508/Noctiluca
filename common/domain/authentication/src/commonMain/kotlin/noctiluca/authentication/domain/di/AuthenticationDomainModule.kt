package noctiluca.authentication.domain.di

import noctiluca.authentication.domain.usecase.*
import noctiluca.authentication.domain.usecase.internal.*
import noctiluca.authentication.domain.usecase.internal.FetchMastodonInstanceUseCaseImpl
import noctiluca.authentication.domain.usecase.internal.RequestAccessTokenUseCaseImpl
import noctiluca.authentication.domain.usecase.internal.RequestAppCredentialUseCaseImpl
import noctiluca.authentication.domain.usecase.internal.SearchMastodonInstancesUseCaseImpl
import org.koin.dsl.ScopeDSL

@Suppress("FunctionName")
fun ScopeDSL.AuthenticationDomainModule() {
    scoped<SearchMastodonInstancesUseCase> { SearchMastodonInstancesUseCaseImpl(get()) }
    scoped<FetchMastodonInstanceUseCase> { FetchMastodonInstanceUseCaseImpl(get()) }
    scoped<FetchLocalTimelineUseCase> { FetchLocalTimelineUseCaseImpl(get()) }
    scoped<RequestAppCredentialUseCase> { RequestAppCredentialUseCaseImpl(get()) }
    scoped<RequestAccessTokenUseCase> { RequestAccessTokenUseCaseImpl(get()) }
}
