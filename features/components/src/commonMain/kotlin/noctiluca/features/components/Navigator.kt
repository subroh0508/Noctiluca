package noctiluca.features.components

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import noctiluca.model.Uri
import org.koin.core.component.KoinScopeComponent

interface Navigator {
    interface Destination
    interface Config
    interface Screen : LifecycleRegistry, ComponentContext, KoinScopeComponent {
        private class Impl(
            componentContext: ComponentContext,
            koinScopeComponent: KoinScopeComponent,
            lifecycleRegistry: LifecycleRegistry,
        ) : Screen,
            ComponentContext by componentContext,
            KoinScopeComponent by koinScopeComponent,
            LifecycleRegistry by lifecycleRegistry

        companion object {
            operator fun invoke(
                koinScopeComponent: KoinScopeComponent,
                lifecycleRegistry: LifecycleRegistry,
            ): Screen = Impl(
                DefaultComponentContext(lifecycleRegistry),
                koinScopeComponent,
                lifecycleRegistry,
            )
        }
    }

    fun backPressed()
    fun openBrowser(uri: Uri)
    fun reopenApp()
    fun backToSignIn()
}
