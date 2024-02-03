package noctiluca.data.di

import noctiluca.data.accountdetail.AccountDetailRepository
import noctiluca.data.accountdetail.AccountRelationshipsRepository
import noctiluca.data.accountdetail.AccountStatusRepository
import noctiluca.data.accountdetail.impl.AccountDetailRepositoryImpl
import noctiluca.data.accountdetail.impl.AccountRelationshipsRepositoryImpl
import noctiluca.data.accountdetail.impl.AccountStatusRepositoryImpl
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.DataAccountDetailModule() {
    single<AccountDetailRepository> { AccountDetailRepositoryImpl(get()) }
    single<AccountRelationshipsRepository> { AccountRelationshipsRepositoryImpl(get(), get()) }
    single<AccountStatusRepository> { AccountStatusRepositoryImpl(get(), get()) }
}
