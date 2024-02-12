package noctiluca.data.di

import noctiluca.data.authorization.AuthorizedUserRepository
import noctiluca.data.authorization.impl.AuthorizedUserRepositoryImpl
import org.koin.dsl.module

@Suppress("FunctionName")
fun DataModule() = module {
    single<AuthorizedUserRepository> { AuthorizedUserRepositoryImpl(get()) }
    DataAccountModule()
    DataAccountDetailModule()
    DataStatusModule()
    DataTimelineModule()
}
