package noctiluca.datastore.di

import kotlinx.serialization.json.Json
import org.koin.core.module.Module

@Suppress("FunctionName")
expect fun Module.TokenDataStoreModule(json: Json)
