package noctiluca.data.di

import noctiluca.data.accountdetail.AccountAttributesRepository
import noctiluca.data.accountdetail.AccountRelationshipsRepository
import noctiluca.data.accountdetail.AccountStatusRepository
import noctiluca.data.accountdetail.impl.AccountAttributesRepositoryImpl
import noctiluca.data.accountdetail.impl.AccountRelationshipsRepositoryImpl
import noctiluca.data.accountdetail.impl.AccountStatusRepositoryImpl
import org.koin.dsl.ScopeDSL

@Suppress("FunctionName")
fun ScopeDSL.DataAccountDetailModule() {
    scoped<AccountAttributesRepository> { AccountAttributesRepositoryImpl(get()) }
    scoped<AccountRelationshipsRepository> { AccountRelationshipsRepositoryImpl(get(), get()) }
    scoped<AccountStatusRepository> { AccountStatusRepositoryImpl(get(), get()) }
}
