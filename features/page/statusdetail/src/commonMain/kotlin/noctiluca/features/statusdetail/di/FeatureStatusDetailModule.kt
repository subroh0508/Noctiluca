package noctiluca.features.statusdetail.di

import noctiluca.features.statusdetail.viewmodel.StatusDetailViewModel
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.FeatureStatusDetailModule() {
    factory { params -> StatusDetailViewModel(params.get(), get(), get(), get()) }
}
