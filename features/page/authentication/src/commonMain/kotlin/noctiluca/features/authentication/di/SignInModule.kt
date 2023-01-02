package noctiluca.features.authentication.di

import noctiluca.authentication.domain.di.AuthenticationDomainModule
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.newScope
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.scope.Scope
import org.koin.dsl.module

class SignInModule : KoinScopeComponent {
    override val scope: Scope by newScope()

    private val module by lazy {
        module {
            scope(scope.scopeQualifier) {
                AuthenticationDomainModule()
            }
        }
    }

    init { loadKoinModules(module) }

    override fun closeScope() {
        super.closeScope()

        unloadKoinModules(module)
    }
}
