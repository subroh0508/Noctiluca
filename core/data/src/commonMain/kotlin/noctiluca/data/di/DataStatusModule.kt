package noctiluca.data.di

import noctiluca.data.status.StatusIndexRepository
import noctiluca.data.status.StatusRepository
import noctiluca.data.status.impl.StatusIndexRepositoryImpl
import noctiluca.data.status.impl.StatusRepositoryImpl
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.DataStatusModule() {
    single<StatusRepository> { StatusRepositoryImpl(get(), get()) }
    single<StatusIndexRepository> { StatusIndexRepositoryImpl(get(), get()) }
}
