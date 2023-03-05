package noctiluca.account.infra.di

import kotlinx.serialization.json.Json
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.AccountInfraModule(json: Json) {
    AccountCredentialCacheModule(json)
    AccountRepositoriesModule()
}
