package noctiluca.data.di

import noctiluca.data.instance.InstanceRepository
import noctiluca.data.instance.impl.InstanceRepositoryImpl
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.DataInstanceModule() {
    single<InstanceRepository> { InstanceRepositoryImpl(get(), get(), get()) }
}
