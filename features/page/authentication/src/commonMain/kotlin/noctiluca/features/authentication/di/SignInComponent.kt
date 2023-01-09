package noctiluca.features.authentication.di

import noctiluca.authentication.domain.di.AuthenticationDomainModule
import noctiluca.features.components.di.FeatureComponent
import noctiluca.features.components.utils.Browser
import org.koin.dsl.module

class SignInComponent(browser: Browser) : FeatureComponent({ scope ->
    module {
        scope(scope.scopeQualifier) {
            scoped { browser }
            AuthenticationDomainModule()
        }
    }
})
