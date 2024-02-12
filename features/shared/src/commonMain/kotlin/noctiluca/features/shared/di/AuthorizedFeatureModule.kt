package noctiluca.features.shared.di

import noctiluca.data.di.AuthorizedContext
import noctiluca.features.shared.viewmodel.AuthorizedContextViewModel
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.AuthorizedFeatureModule() {
    single<AuthorizedContext> { AuthorizedContextViewModel(get()) }
}
