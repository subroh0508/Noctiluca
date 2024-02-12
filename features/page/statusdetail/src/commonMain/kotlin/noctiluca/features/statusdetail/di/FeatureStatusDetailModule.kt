package noctiluca.features.statusdetail.di

import noctiluca.data.di.AuthorizedContext
import noctiluca.features.statusdetail.viewmodel.StatusDetailViewModel
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.FeatureStatusDetailModule() {
    scope<AuthorizedContext> {
        scoped { params -> StatusDetailViewModel(params.get(), get(), get()) }
    }
}
