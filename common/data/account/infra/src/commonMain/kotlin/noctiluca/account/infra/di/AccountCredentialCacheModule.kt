package noctiluca.account.infra.di

import kotlinx.serialization.json.Json
import org.koin.core.module.Module

@Suppress("FunctionName")
expect fun Module.AccountCredentialCacheModule(json: Json)
