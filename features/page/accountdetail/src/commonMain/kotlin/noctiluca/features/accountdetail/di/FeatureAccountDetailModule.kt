package noctiluca.features.accountdetail.di

import noctiluca.features.accountdetail.AccountDetailScreen
import noctiluca.features.accountdetail.viewmodel.AccountAttributesViewModel
import noctiluca.features.accountdetail.viewmodel.AccountRelationshipsViewModel
import noctiluca.features.accountdetail.viewmodel.AccountStatusesViewModel
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.FeatureAccountDetailModule() {
    scope<AccountDetailScreen> {
        scoped { params -> AccountAttributesViewModel(params.get(), get(), get()) }
        scoped { params -> AccountRelationshipsViewModel(params.get(), get(), get()) }
        scoped { params -> AccountStatusesViewModel(params.get(), get(), get()) }
    }
}
