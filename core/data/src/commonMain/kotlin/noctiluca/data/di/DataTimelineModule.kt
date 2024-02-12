package noctiluca.data.di

import noctiluca.data.timeline.TimelineRepository
import noctiluca.data.timeline.impl.TimelineRepositoryImpl
import org.koin.core.module.Module
import org.koin.dsl.ScopeDSL

@Suppress("FunctionName")
fun ScopeDSL.DataTimelineModule() {
    scoped<TimelineRepository> { TimelineRepositoryImpl(get(), get()) }
}

@Suppress("FunctionName")
fun Module.TestDataTimelineModule() {
    single<TimelineRepository> { TimelineRepositoryImpl(get(), get()) }
}
