package noctiluca.timeline.domain.di

import noctiluca.data.timeline.TimelineRepository
import noctiluca.data.timeline.impl.TimelineStreamStateFlow
import noctiluca.timeline.domain.usecase.*
import noctiluca.timeline.domain.usecase.internal.*
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.TimelineDomainModule() {
    single<TimelineStreamStateFlow> { get<TimelineRepository>().stream }
    single<SwitchAuthorizedAccountUseCase> { SwitchAuthorizedAccountUseCaseImpl(get(), get()) }
    single<ExecuteStatusActionUseCase> { ExecuteStatusActionUseCaseImpl(get()) }
    single<SubscribeTimelineStreamUseCase> { SubscribeTimelineStreamUseCaseImpl(get(), get()) }
    single<UnsubscribeTimelineStreamUseCase> { UnsubscribeTimelineStreamUseCaseImpl(get()) }
    single<LoadTimelineStatusesUseCase> { LoadTimelineStatusesUseCaseImpl(get(), get()) }
}
