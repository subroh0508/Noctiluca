package noctiluca.data.di

import noctiluca.data.status.MediaRepository
import noctiluca.data.status.StatusIndexRepository
import noctiluca.data.status.StatusRepository
import noctiluca.data.status.impl.MediaRepositoryImpl
import noctiluca.data.status.impl.StatusIndexRepositoryImpl
import noctiluca.data.status.impl.StatusRepositoryImpl
import org.koin.core.module.Module
import org.koin.dsl.ScopeDSL

@Suppress("FunctionName")
fun ScopeDSL.DataStatusModule() {
    scoped<MediaRepository> { MediaRepositoryImpl(get()) }
    scoped<StatusRepository> { StatusRepositoryImpl(get(), get()) }
    scoped<StatusIndexRepository> { StatusIndexRepositoryImpl(get(), get()) }
}

@Suppress("FunctionName")
fun Module.TestDataStatusModule() {
    single<MediaRepository> { MediaRepositoryImpl(get()) }
    single<StatusRepository> { StatusRepositoryImpl(get(), get()) }
    single<StatusIndexRepository> { StatusIndexRepositoryImpl(get(), get()) }
}
