package noctiluca.features.authentication.di

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import noctiluca.authentication.domain.di.AuthenticationDomainModule
import noctiluca.features.components.di.FeatureComponent
import noctiluca.instance.model.Instance
import noctiluca.model.Uri
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
        class MastodonInstanceList(rootContext: ComponentContext) : Child(), ComponentContext by rootContext

        class MastodonInstanceDetail(
            rootContext: ComponentContext,
            val domain: String,
        ) : Child(), ComponentContext by rootContext
    }

    private sealed class Config : Parcelable {
        @Parcelize
        object MastodonInstanceList : Config()

        @Parcelize
        data class MastodonInstanceDetail(val domain: String) : Config()
    }

    private class MastodonInstanceListState(
        val suggests: List<Instance.Suggest>,
    ) : InstanceKeeper.Instance {
        override fun onDestroy() = Unit
    }

    private val navigation = StackNavigation<Config>()

    val childStack: Value<ChildStack<*, Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.MastodonInstanceList,
        handleBackButton = true,
        childFactory = ::createChild,
    )

    fun restoreSuggests() = (instanceKeeper.remove(KEY) as? MastodonInstanceListState)?.suggests

    fun backPressed() = navigation.pop()

    fun navigateToInstanceDetail(
        suggests: List<Instance.Suggest>,
        domain: String,
    ) {
        instanceKeeper.put(KEY, MastodonInstanceListState(suggests))
        navigation.push(Config.MastodonInstanceDetail(domain))
    }

    private fun createChild(
        config: Config,
        componentContext: ComponentContext,
    ): Child = when (config) {
        is Config.MastodonInstanceList -> Child.MastodonInstanceList(
            componentContext,
        )

        is Config.MastodonInstanceDetail -> Child.MastodonInstanceDetail(
            componentContext,
            config.domain,
        )
    }

    companion object Factory {
        private const val KEY = "SignInFeatureContext"

        operator fun invoke(
            rootContext: ComponentContext,
        ) = SignInFeatureContext(
            rootContext,
            SignInComponent(),
        )
    }
}
