package noctiluca.features.authentication

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import noctiluca.features.authentication.di.SignInComponent
import noctiluca.features.components.Navigator

interface SignInNavigator : Navigator {
    fun navigateToTimelines()

    class Screen(
        private val navigator: SignInNavigator,
        lifecycleRegistry: LifecycleRegistry,
    ) : Navigator.Screen by Navigator.Screen(
        SignInComponent(),
        lifecycleRegistry,
    ),
        SignInNavigator by navigator {
        private val stackNavigation by lazy { StackNavigation<Config>() }

        val childStack: Value<ChildStack<*, Child>> = childStack(
            source = stackNavigation,
            initialConfiguration = Config.MastodonInstanceList,
            handleBackButton = true,
            childFactory = { config, _ ->
                when (config) {
                    is Config.MastodonInstanceList -> Child.MastodonInstanceList
                    is Config.MastodonInstanceDetail -> Child.MastodonInstanceDetail(config.domain)
                }
            },
        )

        override fun backPressed() {
            if (childStack.value.backStack.isNotEmpty()) {
                stackNavigation.pop()
            } else {
                navigator.backPressed()
            }
        }

        sealed class Child : Navigator.Destination {
            object MastodonInstanceList : Child()
            class MastodonInstanceDetail(val domain: String) : Child()
        }

        private sealed class Config : Navigator.Config, Parcelable {
            @Parcelize
            object MastodonInstanceList : Config()

            @Parcelize
            data class MastodonInstanceDetail(val domain: String) : Config()
        }

        companion object {
            @Composable
            operator fun invoke(
                navigator: SignInNavigator,
            ): Screen {
                val lifecycleRegistry = remember { LifecycleRegistry() }

                return remember { Screen(navigator, lifecycleRegistry) }
            }
        }
    }
}
