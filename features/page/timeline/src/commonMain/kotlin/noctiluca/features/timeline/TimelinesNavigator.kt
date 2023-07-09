package noctiluca.features.timeline

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import noctiluca.features.components.Navigator
import noctiluca.features.components.PageContext
import noctiluca.features.timeline.di.TimelineComponent
import org.koin.core.component.KoinScopeComponent

interface TimelinesNavigator : PageContext, Navigator {
    companion object {
        @Composable
        operator fun invoke(): TimelinesNavigator {
            val lifecycleRegistry = remember { LifecycleRegistry() }

            return remember {
                Impl(
                    StackNavigation(),
                    lifecycleRegistry,
                    TimelineComponent(),
                )
            }
        }
    }

    val childStack: Value<ChildStack<*, Child>>

    fun navigateToAccountDetail(id: String)
    fun navigateToToot()

    sealed class Child : ComponentContext, KoinScopeComponent {
        class Timelines(
            navigator: TimelinesNavigator,
            context: ComponentContext,
        ) : Child(), ComponentContext by context, KoinScopeComponent by navigator

        class AccountDetail(
            val id: String,
            navigator: TimelinesNavigator,
            context: ComponentContext,
        ) : Child(), ComponentContext by context, KoinScopeComponent by navigator

        class Toot(
            navigator: TimelinesNavigator,
            context: ComponentContext,
        ) : Child(), ComponentContext by context, KoinScopeComponent by navigator
    }

    private sealed class Config : Parcelable {
        @Parcelize
        object Timelines : Config()

        @Parcelize
        data class AccountDetail(val id: String) : Config()

        @Parcelize
        object Toot : Config()
    }

    private class Impl(
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
        TimelinesNavigator {
        override val childStack: Value<ChildStack<*, Child>> = childStack(
            source = navigation,
            initialConfiguration = Config.Timelines,
            handleBackButton = true,
            childFactory = ::createChild,
        )

        override fun navigateToAccountDetail(id: String) {
            navigation.push(Config.AccountDetail(id))
        }

        override fun navigateToToot() {
            navigation.push(Config.Toot)
        }

        private fun createChild(
            config: Config,
            componentContext: ComponentContext,
        ): Child = when (config) {
            is Config.Timelines -> Child.Timelines(this, componentContext)
            is Config.AccountDetail -> Child.AccountDetail(config.id, this, componentContext)
            is Config.Toot -> Child.Toot(this, componentContext)
        }
    }
}
