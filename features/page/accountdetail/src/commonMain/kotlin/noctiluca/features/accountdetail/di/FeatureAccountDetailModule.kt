package noctiluca.features.accountdetail.di

import noctiluca.features.accountdetail.viewmodel.AccountDetailViewModel
import noctiluca.features.accountdetail.viewmodel.AccountRelationshipsViewModel
import noctiluca.features.shared.EventStateFlow
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.FeatureAccountDetailModule() {
    factory { EventStateFlow() }
    factory { params -> AccountDetailViewModel(params.get(), get(), get(), get(), get()) }
    factory { params -> AccountRelationshipsViewModel(params.get(), get(), get(), get()) }
}
