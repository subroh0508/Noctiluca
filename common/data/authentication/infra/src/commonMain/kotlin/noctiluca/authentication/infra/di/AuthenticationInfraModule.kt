package noctiluca.authentication.infra.di

import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.AuthenticationInfraModule() {
    LocalTokenRepositoriesModule()
    AuthenticationRepositoriesModule()
}
