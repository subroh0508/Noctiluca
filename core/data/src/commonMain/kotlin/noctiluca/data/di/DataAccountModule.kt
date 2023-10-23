package noctiluca.data.di

import noctiluca.data.account.AuthorizedAccountRepository
import noctiluca.data.account.impl.AuthorizedAccountRepositoryImpl
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.DataAccountModule() {
    single<AuthorizedAccountRepository> { AuthorizedAccountRepositoryImpl(get(), get(), get()) }
}
