package noctiluca.features.accountdetail.di

import noctiluca.features.accountdetail.viewmodel.AccountDetailViewModel
import noctiluca.features.accountdetail.viewmodel.AccountRelationshipsViewModel
import noctiluca.features.accountdetail.viewmodel.AccountStatusesViewModel
import noctiluca.features.shared.di.AuthorizedFeatureModule
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.FeatureAccountDetailModule() {
    AuthorizedFeatureModule()
    factory { params -> AccountDetailViewModel(params.get(), get(), get(), get()) }
    factory { params -> AccountRelationshipsViewModel(params.get(), get(), get(), get()) }
    factory { params -> AccountStatusesViewModel(params.get(), get(), get(), get()) }
}
