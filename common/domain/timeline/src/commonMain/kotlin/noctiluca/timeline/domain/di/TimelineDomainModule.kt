package noctiluca.timeline.domain.di

import noctiluca.timeline.domain.usecase.FetchAllAuthorizedAccountsUseCase
import noctiluca.timeline.domain.usecase.FetchCurrentAuthorizedAccountUseCase
import noctiluca.timeline.domain.usecase.UpdateTimelineUseCase
import noctiluca.timeline.domain.usecase.internal.FetchAllAuthorizedAccountsUseCaseImpl
import noctiluca.timeline.domain.usecase.internal.FetchCurrentAuthorizedAccountUseCaseImpl
import noctiluca.timeline.domain.usecase.internal.UpdateTimelineUseCaseImpl
import org.koin.core.module.Module
import org.koin.dsl.ScopeDSL

@Suppress("FunctionName")
fun ScopeDSL.TimelineDomainModule() {
    scoped<FetchCurrentAuthorizedAccountUseCase> { FetchCurrentAuthorizedAccountUseCaseImpl(get()) }
    scoped<FetchAllAuthorizedAccountsUseCase> { FetchAllAuthorizedAccountsUseCaseImpl(get()) }
    scoped<UpdateTimelineUseCase> { UpdateTimelineUseCaseImpl(get()) }
}
