package noctiluca.features.shared.di

import noctiluca.features.shared.AuthorizeEventStateFlow
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.AuthorizedFeatureModule() {
    factory { AuthorizeEventStateFlow() }
}
