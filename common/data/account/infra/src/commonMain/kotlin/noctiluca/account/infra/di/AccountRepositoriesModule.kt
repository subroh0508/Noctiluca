package noctiluca.account.infra.di

import kotlinx.serialization.json.Json
import noctiluca.account.infra.repository.AccountRepository
import noctiluca.account.infra.repository.impl.AccountRepositoryImpl
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.AccountRepositoriesModule(json: Json) {
    AccountCredentialCacheModule(json)
    single<AccountRepository> { AccountRepositoryImpl(get(), get(), get()) }
}
