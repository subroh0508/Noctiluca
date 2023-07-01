package noctiluca.features.authentication.di

import com.arkivanov.decompose.ComponentContext
import noctiluca.authentication.domain.di.AuthenticationDomainModule
import noctiluca.features.components.di.FeatureComponent
import org.koin.core.component.KoinScopeComponent
import org.koin.dsl.module

class SignInFeatureContext private constructor(
    rootContext: ComponentContext,
    koinScopeComponent: KoinScopeComponent,
) : ComponentContext by rootContext, KoinScopeComponent by koinScopeComponent {
    class SignInComponent : FeatureComponent({ scope ->
        module {
            scope(scope.scopeQualifier) {
                AuthenticationDomainModule()
            }
        }
    })

    companion object Factory {
        operator fun invoke(
            rootContext: ComponentContext,
        ) = SignInFeatureContext(
            rootContext,
            SignInComponent(),
        )
    }
}
