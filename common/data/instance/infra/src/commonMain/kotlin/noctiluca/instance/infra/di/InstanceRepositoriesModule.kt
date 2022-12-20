package noctiluca.instance.infra.di

import noctiluca.instance.infra.repository.InstanceRepository
import noctiluca.instance.infra.repository.impl.InstanceRepositoryImpl
import org.koin.core.module.Module
import org.koin.dsl.module

@Suppress("FunctionName")
fun Module.InstanceRepositoriesModule() {
    single<InstanceRepository> { InstanceRepositoryImpl(get(), get()) }
}
