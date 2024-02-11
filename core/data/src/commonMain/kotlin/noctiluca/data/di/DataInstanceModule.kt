package noctiluca.data.di

import noctiluca.data.instance.InstanceRepository
import noctiluca.data.instance.impl.InstanceRepositoryImpl
import org.koin.dsl.ScopeDSL

@Suppress("FunctionName")
fun ScopeDSL.DataInstanceModule() {
    scoped<InstanceRepository> { InstanceRepositoryImpl(get(), get(), get()) }
}
