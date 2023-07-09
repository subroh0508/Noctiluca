package noctiluca.features.accountdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import noctiluca.features.accountdetail.di.AccountDetailComponent
import noctiluca.features.components.Navigator
import noctiluca.model.AccountId

interface AccountDetailNavigator : Navigator {
    class Screen(
        val id: AccountId,
        navigator: AccountDetailNavigator,
        lifecycleRegistry: LifecycleRegistry,
    ) : Navigator.Screen by Navigator.Screen(
        AccountDetailComponent(),
        lifecycleRegistry,
    ),
        AccountDetailNavigator by navigator {
        companion object {
            @Composable
            operator fun invoke(
                id: AccountId,
                navigator: AccountDetailNavigator,
            ): Screen {
                val lifecycleRegistry = remember { LifecycleRegistry() }

                return remember { Screen(id, navigator, lifecycleRegistry) }
            }
        }
    }
}
