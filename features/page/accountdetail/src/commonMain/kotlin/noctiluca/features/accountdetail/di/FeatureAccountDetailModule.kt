package noctiluca.features.accountdetail.di

import noctiluca.accountdetail.domain.di.AccountDetailDomainModule
import noctiluca.features.accountdetail.viewmodel.AccountDetailViewModel
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.FeatureAccountDetailModule() {
    AccountDetailDomainModule()
    factory { params -> AccountDetailViewModel(params.get(), get(), get(), get()) }
}
