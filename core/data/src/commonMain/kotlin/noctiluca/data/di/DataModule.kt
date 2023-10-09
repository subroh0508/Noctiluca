package noctiluca.data.di

import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.DataModule() {
    DataAuthenticationModule()
    DataAccountDetailModule()
}
