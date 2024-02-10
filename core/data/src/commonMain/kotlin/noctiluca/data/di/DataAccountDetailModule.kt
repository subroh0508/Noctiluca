package noctiluca.data.di

import noctiluca.data.accountdetail.AccountAttributesRepository
import noctiluca.data.accountdetail.AccountRelationshipsRepository
import noctiluca.data.accountdetail.AccountStatusRepository
import noctiluca.data.accountdetail.impl.AccountAttributesRepositoryImpl
import noctiluca.data.accountdetail.impl.AccountRelationshipsRepositoryImpl
import noctiluca.data.accountdetail.impl.AccountStatusRepositoryImpl
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.DataAccountDetailModule() {
    single<AccountAttributesRepository> { AccountAttributesRepositoryImpl(get()) }
    single<AccountRelationshipsRepository> { AccountRelationshipsRepositoryImpl(get(), get()) }
    single<AccountStatusRepository> { AccountStatusRepositoryImpl(get(), get()) }
}
