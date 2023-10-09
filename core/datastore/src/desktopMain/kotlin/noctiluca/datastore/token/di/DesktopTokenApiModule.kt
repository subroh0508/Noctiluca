package noctiluca.datastore.token.di

import kotlinx.serialization.json.Json
import noctiluca.datastore.JsonPreferences
import noctiluca.datastore.token.LocalTokenCache
import org.koin.core.module.Module

@Suppress("FunctionName")
actual fun Module.TokenApiModule(json: Json) {
    single { LocalTokenCache(JsonPreferences(json, listOf(), get())) }
    // single<TokenCache> { get<LocalTokenCache>() }
}
