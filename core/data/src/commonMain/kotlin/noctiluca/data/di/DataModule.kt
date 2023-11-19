package noctiluca.data.di

import org.koin.dsl.module

@Suppress("FunctionName")
fun DataModule() = module {
    DataAccountModule()
    DataAccountDetailModule()
    DataAuthenticationModule()
    DataInstanceModule()
    DataStatusModule()
    DataTimelineModule()
}
