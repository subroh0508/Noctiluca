package noctiluca.timeline.domain.di

import noctiluca.timeline.domain.usecase.*
import noctiluca.timeline.domain.usecase.internal.*
import noctiluca.timeline.domain.usecase.internal.FetchAllAuthorizedAccountsUseCaseImpl
import noctiluca.timeline.domain.usecase.internal.FetchCurrentAuthorizedAccountUseCaseImpl
import noctiluca.timeline.domain.usecase.internal.FetchTimelineStreamUseCaseImpl
import noctiluca.timeline.domain.usecase.internal.UpdateTimelineUseCaseImpl
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.TimelineDomainModule() {
    single<FetchCurrentAuthorizedAccountUseCase> { FetchCurrentAuthorizedAccountUseCaseImpl(get()) }
    single<FetchAllAuthorizedAccountsUseCase> { FetchAllAuthorizedAccountsUseCaseImpl(get()) }
    single<FetchTimelineStreamUseCase> { FetchTimelineStreamUseCaseImpl(get()) }
    single<UpdateTimelineUseCase> { UpdateTimelineUseCaseImpl(get()) }
    single<ExecuteStatusActionUseCase> { ExecuteStatusActionUseCaseImpl(get()) }
}
