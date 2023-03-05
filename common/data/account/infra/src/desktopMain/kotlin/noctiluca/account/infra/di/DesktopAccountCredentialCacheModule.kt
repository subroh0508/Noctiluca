package noctiluca.account.infra.di

import kotlinx.serialization.json.Json
import noctiluca.account.infra.repository.local.DesktopLocalAccountCredentialCache
import noctiluca.account.infra.repository.local.LocalAccountCredentialCache
import org.koin.core.module.Module

@Suppress("FunctionName")
actual fun Module.AccountCredentialCacheModule(json: Json) {
    single<LocalAccountCredentialCache> { DesktopLocalAccountCredentialCache(get(), get()) }
}