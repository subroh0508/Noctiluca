package noctiluca.api.token.di

import kotlinx.serialization.json.Json
import org.koin.core.module.Module

@Suppress("FunctionName")
expect fun Module.TokenApiModule(json: Json)
