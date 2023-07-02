package noctiluca.features.authentication.viewmodel.navigator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import noctiluca.authentication.domain.di.AuthenticationDomainModule
import noctiluca.features.components.PageContext
import noctiluca.features.components.di.FeatureComponent
import org.koin.core.component.KoinScopeComponent
import org.koin.dsl.module

class SignInNavigator private constructor(
    lifecycleRegistry: LifecycleRegistry,
    componentContext: ComponentContext,
    koinScopeComponent: KoinScopeComponent,
) : PageContext(KEY, lifecycleRegistry, componentContext, koinScopeComponent) {
    class SignInComponent : FeatureComponent({ scope ->
        module {
            scope(scope.scopeQualifier) {
                AuthenticationDomainModule()
            }
        }
    })

    sealed class Child {
        class MastodonInstanceList(
            childContext: ComponentContext,
            koinScopeComponent: KoinScopeComponent,
        ) : Child(), ComponentContext by childContext, KoinScopeComponent by koinScopeComponent

        class MastodonInstanceDetail(
            val domain: String,
            childContext: ComponentContext,
            koinScopeComponent: KoinScopeComponent,
        ) : Child(), ComponentContext by childContext, KoinScopeComponent by koinScopeComponent
    }

    private sealed class Config : Parcelable {
        @Parcelize
        object MastodonInstanceList : Config()

        @Parcelize
        data class MastodonInstanceDetail(val domain: String) : Config()
    }

    private val navigation = StackNavigation<Config>()

    val childStack: Value<ChildStack<*, Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.MastodonInstanceList,
        handleBackButton = true,
        childFactory = ::createChild,
    )

    fun backPressed() = navigation.pop()

    fun navigateToInstanceDetail(
        domain: String,
    ) = navigation.push(Config.MastodonInstanceDetail(domain))

    private fun createChild(
        config: Config,
        componentContext: ComponentContext,
    ): Child = when (config) {
        is Config.MastodonInstanceList -> Child.MastodonInstanceList(
            componentContext,
            this,
        )

        is Config.MastodonInstanceDetail -> Child.MastodonInstanceDetail(
            config.domain,
            componentContext,
            this,
        )
    }

    companion object Factory {
        private const val KEY = "SignInPage"

        @Composable
        operator fun invoke(
            rootContext: ComponentContext,
        ): SignInNavigator {
            val lifecycleRegistry = remember { LifecycleRegistry() }

            return remember {
                SignInNavigator(
                    lifecycleRegistry,
                    rootContext,
                    SignInComponent(),
                )
            }
        }
    }
}
