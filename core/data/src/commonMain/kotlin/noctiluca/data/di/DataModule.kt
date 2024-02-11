package noctiluca.data.di

import noctiluca.data.authentication.AuthorizedUserRepository
import noctiluca.data.authentication.impl.AuthorizedUserRepositoryImpl
import org.koin.dsl.module

@Suppress("FunctionName")
fun DataModule() = module {
    single<AuthorizedUserRepository> { AuthorizedUserRepositoryImpl(get()) }
    DataAccountModule()
    DataAccountDetailModule()
    DataStatusModule()
    DataTimelineModule()
}
