package noctiluca.features.signin.di

import noctiluca.data.di.DataAuthorizationModule
import noctiluca.data.di.DataInstanceModule
import noctiluca.features.signin.SignInScreen
import noctiluca.features.signin.viewmodel.AuthorizeViewModel
import noctiluca.features.signin.viewmodel.MastodonInstanceDetailViewModel
import noctiluca.features.signin.viewmodel.MastodonInstanceListViewModel
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.FeatureSignInModule() {
    scope<SignInScreen> {
        DataAuthorizationModule()
        DataInstanceModule()
        scoped { params -> AuthorizeViewModel(params.get(), params.get(), get(), get()) }
        scoped { MastodonInstanceListViewModel(get()) }
        scoped { MastodonInstanceDetailViewModel(get()) }
    }
}
