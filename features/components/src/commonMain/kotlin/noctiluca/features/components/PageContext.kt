package noctiluca.features.components

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.subscribe
import org.koin.core.component.KoinScopeComponent

abstract class PageContext private constructor(
    private val lifecycleRegistry: LifecycleRegistry,
    componentContext: ComponentContext,
    koinScopeComponent: KoinScopeComponent,
) : LifecycleRegistry by lifecycleRegistry,
    ComponentContext by componentContext,
    KoinScopeComponent by koinScopeComponent {

    constructor(
        key: String,
        lifecycleRegistry: LifecycleRegistry,
        rootComponentContext: ComponentContext,
        koinScopeComponent: KoinScopeComponent,
    ) : this(
        lifecycleRegistry,
        rootComponentContext.childContext(
            key,
            lifecycleRegistry,
        ),
        koinScopeComponent,
    )

    init {
        lifecycleRegistry.subscribe(
            onDestroy = { scope.close() },
        )
    }
}
