package noctiluca.data.di

import noctiluca.data.timeline.TimelineRepository
import noctiluca.data.timeline.impl.TimelineRepositoryImpl
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.DataTimelineModule() {
    single<TimelineRepository> { TimelineRepositoryImpl(get(), get(), get()) }
}
