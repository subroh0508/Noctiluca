package noctiluca.features.components

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.subscribe
import org.koin.core.component.KoinScopeComponent

interface PageContext : LifecycleRegistry, ComponentContext, KoinScopeComponent {
    companion object {
        operator fun invoke(
            key: String,
            lifecycleRegistry: LifecycleRegistry,
            rootComponentContext: ComponentContext,
            koinScopeComponent: KoinScopeComponent,
        ): PageContext = Impl(
            lifecycleRegistry,
            rootComponentContext.childContext(
                key,
                lifecycleRegistry,
            ),
            koinScopeComponent,
        )
    }

    private class Impl(
        lifecycleRegistry: LifecycleRegistry,
        componentContext: ComponentContext,
        koinScopeComponent: KoinScopeComponent,
    ) : PageContext,
        LifecycleRegistry by lifecycleRegistry,
        ComponentContext by componentContext,
        KoinScopeComponent by koinScopeComponent {

        init {
            lifecycleRegistry.subscribe(
                onDestroy = { scope.close() },
            )
        }
    }
}
