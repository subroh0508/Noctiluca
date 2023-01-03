package noctiluca.features.authentication.di

import noctiluca.authentication.domain.di.AuthenticationDomainModule
import noctiluca.components.di.FeatureComponent
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.newScope
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.scope.Scope
import org.koin.dsl.module

class SignInModule : FeatureComponent({ scope ->
    module {
        scope(scope.scopeQualifier) {
            AuthenticationDomainModule()
        }
    }
})
