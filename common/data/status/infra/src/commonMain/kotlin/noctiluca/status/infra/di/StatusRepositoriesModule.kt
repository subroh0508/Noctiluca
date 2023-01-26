package noctiluca.status.infra.di

import noctiluca.status.infra.repository.StatusRepository
import noctiluca.status.infra.repository.impl.StatusRepositoryImpl
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.StatusRepositoriesModule() {
    single<StatusRepository> { StatusRepositoryImpl(get(), get()) }
}
