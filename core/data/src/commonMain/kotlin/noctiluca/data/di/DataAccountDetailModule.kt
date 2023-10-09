package noctiluca.data.di

import noctiluca.data.accountdetail.AccountDetailRepository
import noctiluca.data.accountdetail.impl.AccountDetailRepositoryImpl
import noctiluca.data.authentication.AppCredentialRepository
import noctiluca.data.authentication.AuthorizedUserRepository
import noctiluca.data.authentication.impl.AppCredentialRepositoryImpl
import noctiluca.data.authentication.impl.AuthorizedUserRepositoryImpl
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.DataAccountDetailModule() {
    single<AccountDetailRepository> { AccountDetailRepositoryImpl(get(), get()) }
}
