package noctiluca.accountdetail.infra.di

import noctiluca.accountdetail.infra.repository.AccountDetailRepository
import noctiluca.accountdetail.infra.repository.impl.AccountDetailRepositoryImpl
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.AccountDetailInfraModule() {
    single<AccountDetailRepository> { AccountDetailRepositoryImpl(get(), get()) }
}
