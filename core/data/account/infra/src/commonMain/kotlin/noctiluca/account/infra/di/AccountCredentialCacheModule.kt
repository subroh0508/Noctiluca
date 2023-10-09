package noctiluca.account.infra.di

import kotlinx.serialization.json.Json
import noctiluca.account.infra.repository.local.LocalAuthorizedAccountRepository
import noctiluca.account.infra.repository.local.impl.LocalAuthorizedAccountRepositoryImpl
import org.koin.core.module.Module

@Suppress("FunctionName")
internal expect fun Module.AccountCredentialCacheModule(json: Json)

@Suppress("FunctionName")
internal fun Module.LocalAccountRepositoriesModule(json: Json) {
    AccountCredentialCacheModule(json)
    single<LocalAuthorizedAccountRepository> { LocalAuthorizedAccountRepositoryImpl(get(), get(), get()) }
}
