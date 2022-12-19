package noctiluca.features.authentication.di

import noctiluca.authentication.domain.di.AuthenticationDomainModule
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.newScope
import org.koin.core.context.loadKoinModules
import org.koin.core.scope.Scope
import org.koin.dsl.module

object SignInModule : KoinScopeComponent {
    override val scope: Scope by newScope()

    operator fun invoke() = module {
        scope(scope.scopeQualifier) {
            AuthenticationDomainModule()
        }
    }
}
