package noctiluca.features.accountdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import noctiluca.features.accountdetail.di.AccountDetailComponent
import noctiluca.features.components.Navigator
import noctiluca.features.components.PageContext
import noctiluca.model.AccountId
import org.koin.core.component.KoinScopeComponent

interface AccountDetailNavigator : PageContext, Navigator {
    companion object {
        @Composable
        operator fun invoke(
            id: String,
        ): AccountDetailNavigator {
            val lifecycleRegistry = remember { LifecycleRegistry() }

            return remember {
                Impl(
                    id,
                    StackNavigation(),
                    lifecycleRegistry,
                    AccountDetailComponent(),
                )
            }
        }
    }

    val childStack: Value<ChildStack<*, Child>>

    sealed class Child {
        class AccountDetail(
            val id: AccountId,
            navigator: AccountDetailNavigator,
            childContext: ComponentContext,
        ) : Child(), ComponentContext by childContext, KoinScopeComponent by navigator
    }

    private sealed class Config : Parcelable {
        @Parcelize
        data class AccountDetail(val id: String) : Config()
    }

    private class Impl(
        private val id: String,
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
        AccountDetailNavigator {
        override val childStack: Value<ChildStack<*, Child>> = childStack(
            source = navigation,
            initialConfiguration = Config.AccountDetail(id),
            handleBackButton = true,
            childFactory = { _, componentContext ->
                Child.AccountDetail(
                    AccountId(id),
                    this,
                    componentContext,
                )
            },
        )
    }
}
