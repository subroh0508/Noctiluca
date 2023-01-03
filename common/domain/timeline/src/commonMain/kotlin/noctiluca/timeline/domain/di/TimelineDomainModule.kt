package noctiluca.timeline.domain.di

import noctiluca.timeline.domain.usecase.FetchCurrentAuthorizedAccountUseCase
import noctiluca.timeline.domain.usecase.internal.FetchCurrentAuthorizedAccountUseCaseImpl
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.TimelineDomainModule() {
    single<FetchCurrentAuthorizedAccountUseCase> { FetchCurrentAuthorizedAccountUseCaseImpl(get()) }
}
