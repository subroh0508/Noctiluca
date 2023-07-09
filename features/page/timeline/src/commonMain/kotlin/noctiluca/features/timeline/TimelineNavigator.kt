package noctiluca.features.timeline

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import noctiluca.features.components.Navigator
import noctiluca.features.timeline.di.TimelineComponent

interface TimelineNavigator : Navigator {
    fun navigateToAccountDetail(id: String)

    class Screen(
        private val navigator: TimelineNavigator,
        lifecycleRegistry: LifecycleRegistry,
    ) : Navigator.Screen by Navigator.Screen(
        TimelineComponent(),
        lifecycleRegistry,
    ), TimelineNavigator by navigator {
        private val stackNavigation by lazy { StackNavigation<Config>() }

        val childStack: Value<ChildStack<*, Child>> = childStack(
            source = stackNavigation,
            initialConfiguration = Config.Timelines,
            handleBackButton = true,
            childFactory = { config, _ ->
                when (config) {
                    is Config.Timelines -> Child.Timelines
                    is Config.Toot -> Child.Toot
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

        fun navigateToToot() {
            stackNavigation.push(Config.Toot)
        }

        sealed class Child : Navigator.Destination {
            object Timelines : Child()
            object Toot : Child()
        }

        private sealed class Config : Navigator.Config, Parcelable {
            @Parcelize
            object Timelines : Config()

            @Parcelize
            object Toot : Config()
        }

        companion object {
            @Composable
            operator fun invoke(
                navigator: TimelineNavigator,
            ): Screen {
                val lifecycleRegistry = remember { LifecycleRegistry() }

                return remember { Screen(navigator, lifecycleRegistry) }
            }
        }
    }
}
