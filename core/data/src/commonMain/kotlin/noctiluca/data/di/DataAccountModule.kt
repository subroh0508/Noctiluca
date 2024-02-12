package noctiluca.data.di

import noctiluca.data.account.AuthorizedAccountRepository
import noctiluca.data.account.impl.AuthorizedAccountRepositoryImpl
import org.koin.core.module.Module
import org.koin.dsl.ScopeDSL

@Suppress("FunctionName")
fun ScopeDSL.DataAccountModule() {
    scoped<AuthorizedAccountRepository> { AuthorizedAccountRepositoryImpl(get(), get(), get()) }
}

@Suppress("FunctionName")
fun Module.TestDataAccountModule() {
    single<AuthorizedAccountRepository> { AuthorizedAccountRepositoryImpl(get(), get(), get()) }
}
