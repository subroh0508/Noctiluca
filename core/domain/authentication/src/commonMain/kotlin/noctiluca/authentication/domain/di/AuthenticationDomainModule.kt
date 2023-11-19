package noctiluca.authentication.domain.di

import noctiluca.authentication.domain.usecase.*
import noctiluca.authentication.domain.usecase.internal.*
import noctiluca.authentication.domain.usecase.internal.FetchMastodonInstanceUseCaseImpl
import noctiluca.authentication.domain.usecase.internal.RequestAccessTokenUseCaseImpl
import noctiluca.authentication.domain.usecase.internal.RequestAppCredentialUseCaseImpl
import noctiluca.authentication.domain.usecase.internal.SearchMastodonInstancesUseCaseImpl
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.AuthenticationDomainModule() {
    single<SearchMastodonInstancesUseCase> { SearchMastodonInstancesUseCaseImpl(get()) }
    single<FetchMastodonInstanceUseCase> { FetchMastodonInstanceUseCaseImpl(get()) }
    single<FetchLocalTimelineUseCase> { FetchLocalTimelineUseCaseImpl(get()) }
    single<RequestAppCredentialUseCase> { RequestAppCredentialUseCaseImpl(get()) }
    single<RequestAccessTokenUseCase> { RequestAccessTokenUseCaseImpl(get()) }
}
