package noctiluca.account.infra.di

import noctiluca.account.infra.repository.AuthorizedAccountRepository
import noctiluca.account.infra.repository.impl.AuthorizedAccountRepositoryImpl
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.AccountRepositoriesModule() {
    single<AuthorizedAccountRepository> { AuthorizedAccountRepositoryImpl(get(), get()) }
}
