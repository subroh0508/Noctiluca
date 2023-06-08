package noctiluca.features.authentication.di

import noctiluca.authentication.domain.di.AuthenticationDomainModule
import noctiluca.features.components.di.FeatureComponent
import org.koin.dsl.module

class SignInComponent : FeatureComponent({ scope ->
    module {
        scope(scope.scopeQualifier) {
            AuthenticationDomainModule()
        }
    }
})
