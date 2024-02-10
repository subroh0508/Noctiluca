package noctiluca.features.authentication.di

import noctiluca.authentication.domain.di.AuthenticationDomainModule
import noctiluca.features.authentication.viewmodel.AuthorizeViewModel
import noctiluca.features.authentication.viewmodel.MastodonInstanceDetailViewModel
import noctiluca.features.authentication.viewmodel.MastodonInstanceListViewModel
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.FeatureSignInModule() {
    AuthenticationDomainModule()
    factory { MastodonInstanceListViewModel(get()) }
    factory { params -> AuthorizeViewModel(params.get(), params.get(), get(), get()) }
    factory { params -> MastodonInstanceDetailViewModel(params.get(), get()) }
}
