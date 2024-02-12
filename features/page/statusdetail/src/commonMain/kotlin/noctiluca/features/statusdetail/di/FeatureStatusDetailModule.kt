package noctiluca.features.statusdetail.di

import noctiluca.features.statusdetail.StatusDetailScreen
import noctiluca.features.statusdetail.viewmodel.StatusDetailViewModel
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.FeatureStatusDetailModule() {
    scope<StatusDetailScreen> {
        scoped { params -> StatusDetailViewModel(params.get(), get(), get()) }
    }
}
