package noctiluca.data.di

import noctiluca.data.accountdetail.AccountDetailRepository
import noctiluca.data.accountdetail.impl.AccountDetailRepositoryImpl
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.DataAccountDetailModule() {
    single<AccountDetailRepository> { AccountDetailRepositoryImpl(get(), get()) }
}
