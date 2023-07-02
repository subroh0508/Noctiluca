package noctiluca.features.authentication.viewmodel.context

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import noctiluca.authentication.domain.di.AuthenticationDomainModule
import noctiluca.features.components.di.FeatureComponent
import noctiluca.instance.model.Instance
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
        operator fun invoke(
            rootContext: ComponentContext,
        ) = SignInFeatureContext(
            rootContext,
            SignInComponent(),
        )
    }
}
