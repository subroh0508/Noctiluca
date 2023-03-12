package noctiluca.api.token.di

import kotlinx.serialization.json.Json
import noctiluca.api.token.LocalTokenCache
import noctiluca.preferences.JsonPreferences
import noctiluca.repository.TokenCache
import org.koin.core.module.Module

@Suppress("FunctionName")
actual fun Module.TokenApiModule(json: Json) {
    single { LocalTokenCache(JsonPreferences(json, listOf(), get())) }
    single<TokenCache> { get<LocalTokenCache>() }
}
