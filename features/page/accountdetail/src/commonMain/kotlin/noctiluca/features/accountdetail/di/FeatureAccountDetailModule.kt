package noctiluca.features.accountdetail.di

import noctiluca.features.accountdetail.viewmodel.AccountDetailViewModel
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.FeatureAccountDetailModule() {
    factory { params -> AccountDetailViewModel(params.get(), get(), get()) }
}
