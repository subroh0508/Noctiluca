package noctiluca.data.di

import noctiluca.data.status.StatusIndexRepository
import noctiluca.data.status.StatusRepository
import noctiluca.data.status.impl.StatusIndexRepositoryImpl
import noctiluca.data.status.impl.StatusRepositoryImpl
import org.koin.core.module.Module
import org.koin.dsl.ScopeDSL

@Suppress("FunctionName")
fun ScopeDSL.DataStatusModule() {
    scoped<StatusRepository> { StatusRepositoryImpl(get(), get()) }
    scoped<StatusIndexRepository> { StatusIndexRepositoryImpl(get(), get()) }
}

@Suppress("FunctionName")
fun Module.TestDataStatusModule() {
    single<StatusRepository> { StatusRepositoryImpl(get(), get()) }
    single<StatusIndexRepository> { StatusIndexRepositoryImpl(get(), get()) }
}
