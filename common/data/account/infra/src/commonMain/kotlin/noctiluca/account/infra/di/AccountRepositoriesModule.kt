package noctiluca.account.infra.di

import noctiluca.account.infra.repository.AccountRepository
import noctiluca.account.infra.repository.impl.AccountRepositoryImpl
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.AccountRepositoriesModule() {
    single<AccountRepository> { AccountRepositoryImpl(get(), get(), get()) }
}
