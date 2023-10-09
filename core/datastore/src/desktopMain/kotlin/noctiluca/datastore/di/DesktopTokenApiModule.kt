package noctiluca.datastore.di

import kotlinx.serialization.json.Json
import noctiluca.datastore.JsonPreferences
import noctiluca.datastore.TokenDataStore
import org.koin.core.module.Module

@Suppress("FunctionName")
actual fun Module.TokenApiModule(json: Json) {
    single { TokenDataStore(JsonPreferences(json, listOf(), get())) }
}
