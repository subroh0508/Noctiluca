package noctiluca.features.authentication.di

import noctiluca.features.authentication.viewmodel.AuthorizeViewModel
import noctiluca.features.authentication.viewmodel.MastodonInstanceDetailViewModel
import noctiluca.features.authentication.viewmodel.MastodonInstanceListViewModel
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.FeatureSignInModule() {
    factory { params -> AuthorizeViewModel(params.get(), params.get(), get()) }
    factory { MastodonInstanceListViewModel(get()) }
    factory { MastodonInstanceDetailViewModel(get()) }
}
