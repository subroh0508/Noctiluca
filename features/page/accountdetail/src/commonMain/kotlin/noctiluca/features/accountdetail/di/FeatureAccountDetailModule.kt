package noctiluca.features.accountdetail.di

import noctiluca.data.di.AuthorizedContext
import noctiluca.features.accountdetail.viewmodel.AccountAttributesViewModel
import noctiluca.features.accountdetail.viewmodel.AccountRelationshipsViewModel
import noctiluca.features.accountdetail.viewmodel.AccountStatusesViewModel
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.FeatureAccountDetailModule() {
    scope<AuthorizedContext> {
        factory { params -> AccountAttributesViewModel(params.get(), get(), get()) }
        factory { params -> AccountRelationshipsViewModel(params.get(), get(), get()) }
        factory { params -> AccountStatusesViewModel(params.get(), get(), get()) }
    }
}
