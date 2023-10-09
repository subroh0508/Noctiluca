package noctiluca.account.infra.di

import android.app.Application
import kotlinx.serialization.json.Json
import noctiluca.account.infra.repository.local.LocalAccountCredentialCache
import org.koin.core.module.Module

@Suppress("FunctionName")
internal actual fun Module.AccountCredentialCacheModule(json: Json) {
    single { LocalAccountCredentialCache(get<Application>(), json) }
}
