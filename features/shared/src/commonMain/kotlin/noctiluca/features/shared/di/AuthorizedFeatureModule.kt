package noctiluca.features.shared.di

import noctiluca.features.shared.context.AuthorizedContext
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.AuthorizedFeatureModule() {
    single { AuthorizedContext(get()) }
}
