package noctiluca.features.authentication

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import noctiluca.features.authentication.di.SignInComponent
import noctiluca.features.components.Navigator
import noctiluca.features.components.PageContext
import noctiluca.model.Uri
import org.koin.core.component.KoinScopeComponent

interface SignInNavigator : PageContext, Navigator {
    companion object {
        @Composable
        operator fun invoke(
            nav: SignInNavigation,
        ): SignInNavigator {
            val lifecycleRegistry = remember { LifecycleRegistry() }

            return remember {
                Impl(
                    nav = nav,
                    StackNavigation(),
                    lifecycleRegistry,
                    SignInComponent(),
                )
            }
        }
    }

    val childStack: Value<ChildStack<*, Child>>

    fun openBrowser(uri: Uri)
    fun navigateToTimelines()
    fun navigateToInstanceDetail(domain: String)

    sealed class Child {
        class MastodonInstanceList(
            navigator: SignInNavigator,
            childContext: ComponentContext,
        ) : Child(), ComponentContext by childContext, KoinScopeComponent by navigator

        class MastodonInstanceDetail(
            val domain: String,
            navigator: SignInNavigator,
            childContext: ComponentContext,
        ) : Child(), ComponentContext by childContext, KoinScopeComponent by navigator
    }

    private sealed class Config : Parcelable {
        @Parcelize
        object MastodonInstanceList : Config()

        @Parcelize
        data class MastodonInstanceDetail(val domain: String) : Config()
    }

    private class Impl(
        private val nav: SignInNavigation,
        private val navigation: StackNavigation<Config>,
        lifecycleRegistry: LifecycleRegistry,
        koinScopeComponent: KoinScopeComponent,
    ) : PageContext by PageContext(
        lifecycleRegistry,
        koinScopeComponent,
    ),
        Navigator by Navigator(
            navigation,
        ),
        SignInNavigator {
        override val childStack: Value<ChildStack<*, Child>> = childStack(
            source = navigation,
            initialConfiguration = Config.MastodonInstanceList,
            handleBackButton = true,
            childFactory = ::createChild,
        )

        override fun openBrowser(uri: Uri) = nav.openBrowser(uri)

        override fun navigateToInstanceDetail(
            domain: String,
        ) = navigation.push(Config.MastodonInstanceDetail(domain))

        override fun navigateToTimelines() = nav.navigateToTimelines()

        private fun createChild(
            config: Config,
            componentContext: ComponentContext,
        ): Child = when (config) {
            is Config.MastodonInstanceList -> Child.MastodonInstanceList(
                this,
                componentContext,
            )

            is Config.MastodonInstanceDetail -> Child.MastodonInstanceDetail(
                config.domain,
                this,
                componentContext,
            )
        }
    }
}
