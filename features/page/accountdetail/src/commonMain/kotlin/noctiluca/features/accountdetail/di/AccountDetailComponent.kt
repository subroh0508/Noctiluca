package noctiluca.features.accountdetail.di

import noctiluca.accountdetail.domain.di.AccountDetailDomainModule
import noctiluca.features.shared.di.FeatureComponent
import org.koin.dsl.module

class AccountDetailComponent : FeatureComponent({ scope ->
    module {
        scope(scope.scopeQualifier) {
            AccountDetailDomainModule()
        }
    }
})
