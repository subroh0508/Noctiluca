package noctiluca.datastore.di

import kotlinx.serialization.json.Json
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.DataStoreModule(json: Json) {
    AppCredentialDataStoreModule()
    AuthorizationTokenDataStoreModule(json)
    AccountDataStoreModule(json)
}
