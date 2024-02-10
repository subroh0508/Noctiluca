package noctiluca.authentication.domain.di

import noctiluca.authentication.domain.usecase.*
import noctiluca.authentication.domain.usecase.internal.RequestAccessTokenUseCaseImpl
import noctiluca.authentication.domain.usecase.internal.RequestAppCredentialUseCaseImpl
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.AuthenticationDomainModule() {
    single<RequestAppCredentialUseCase> { RequestAppCredentialUseCaseImpl(get()) }
    single<RequestAccessTokenUseCase> { RequestAccessTokenUseCaseImpl(get()) }
}
