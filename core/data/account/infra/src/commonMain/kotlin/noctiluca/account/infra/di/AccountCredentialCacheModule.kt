package noctiluca.account.infra.di

import kotlinx.serialization.json.Json
import noctiluca.account.infra.repository.local.LocalAuthorizedAccountRepository
import noctiluca.account.infra.repository.local.impl.LocalAuthorizedAccountRepositoryImpl
import noctiluca.datastore.di.AccountDataStoreModule
import org.koin.core.module.Module

@Suppress("FunctionName")
internal fun Module.LocalAccountRepositoriesModule(json: Json) {
    AccountDataStoreModule(json)
    single<LocalAuthorizedAccountRepository> {
        LocalAuthorizedAccountRepositoryImpl(get(), get(), get())
    }
}
