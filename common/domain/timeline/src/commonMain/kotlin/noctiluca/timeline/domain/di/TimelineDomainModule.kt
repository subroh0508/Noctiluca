package noctiluca.timeline.domain.di

import noctiluca.timeline.domain.usecase.*
import noctiluca.timeline.domain.usecase.internal.*
import noctiluca.timeline.domain.usecase.internal.FetchAllAuthorizedAccountsUseCaseImpl
import noctiluca.timeline.domain.usecase.internal.FetchCurrentAuthorizedAccountUseCaseImpl
import noctiluca.timeline.domain.usecase.internal.FetchTimelineStreamUseCaseImpl
import noctiluca.timeline.domain.usecase.internal.UpdateTimelineUseCaseImpl
import org.koin.dsl.ScopeDSL

@Suppress("FunctionName")
fun ScopeDSL.TimelineDomainModule() {
    scoped<FetchCurrentAuthorizedAccountUseCase> { FetchCurrentAuthorizedAccountUseCaseImpl(get()) }
    scoped<FetchAllAuthorizedAccountsUseCase> {
        FetchAllAuthorizedAccountsUseCaseImpl(
            get(),
            get()
        )
    }
    scoped<FetchTimelineStreamUseCase> { FetchTimelineStreamUseCaseImpl(get()) }
    scoped<UpdateTimelineUseCase> { UpdateTimelineUseCaseImpl(get()) }
    scoped<ExecuteStatusActionUseCase> { ExecuteStatusActionUseCaseImpl(get()) }
}
