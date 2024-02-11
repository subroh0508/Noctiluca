package noctiluca.features.authentication.di

import noctiluca.data.di.DataAuthenticationModule
import noctiluca.data.di.DataInstanceModule
import noctiluca.features.authentication.SignInScreen
import noctiluca.features.authentication.viewmodel.AuthorizeViewModel
import noctiluca.features.authentication.viewmodel.MastodonInstanceDetailViewModel
import noctiluca.features.authentication.viewmodel.MastodonInstanceListViewModel
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.FeatureSignInModule() {
    scope<SignInScreen> {
        DataAuthenticationModule()
        DataInstanceModule()
        scoped { params -> AuthorizeViewModel(params.get(), params.get(), get()) }
        scoped { MastodonInstanceListViewModel(get()) }
        scoped { MastodonInstanceDetailViewModel(get()) }
    }
}
