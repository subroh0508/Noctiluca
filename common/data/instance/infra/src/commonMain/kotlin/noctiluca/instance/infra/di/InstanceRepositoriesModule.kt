package noctiluca.instance.infra.di

import noctiluca.instance.infra.repository.InstanceRepository
import noctiluca.instance.infra.repository.impl.InstanceRepositoryImpl
import org.koin.dsl.module

object InstanceRepositoriesModule {
    operator fun invoke() = module {
        single<InstanceRepository> { InstanceRepositoryImpl(get()) }
    }
}
