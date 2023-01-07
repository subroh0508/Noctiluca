package noctiluca.status.infra.di

import noctiluca.status.infra.repository.TimelineRepository
import noctiluca.status.infra.repository.impl.TimelineRepositoryImpl
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.StatusRepositoriesModule() {
    single<TimelineRepository> { TimelineRepositoryImpl(get()) }
}
