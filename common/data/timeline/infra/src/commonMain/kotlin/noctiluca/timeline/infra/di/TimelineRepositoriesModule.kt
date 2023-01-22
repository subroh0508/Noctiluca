package noctiluca.timeline.infra.di

import noctiluca.timeline.infra.repository.TimelineRepository
import noctiluca.timeline.infra.repository.impl.TimelineRepositoryImpl
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.TimelineRepositoriesModule() {
    single<TimelineRepository> { TimelineRepositoryImpl(get(), get(), get()) }
}
