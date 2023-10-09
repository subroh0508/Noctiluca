package noctiluca.data.di

import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.DataModule() {
    DataAccountModule()
    DataAccountDetailModule()
    DataAuthenticationModule()
    DataStatusModule()
    DataTimelineModule()
}
