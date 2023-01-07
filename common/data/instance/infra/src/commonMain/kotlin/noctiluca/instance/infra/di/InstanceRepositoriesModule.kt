package noctiluca.instance.infra.di

import noctiluca.instance.infra.repository.InstanceRepository
import noctiluca.instance.infra.repository.impl.InstanceRepositoryImpl
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.InstanceRepositoriesModule() {
    single<InstanceRepository> { InstanceRepositoryImpl(get(), get()) }
}
