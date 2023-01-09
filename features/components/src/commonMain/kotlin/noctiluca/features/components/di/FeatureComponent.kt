package noctiluca.features.components.di

import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.newScope
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.core.scope.Scope

abstract class FeatureComponent(
    private val builder: (Scope) -> Module,
) : KoinScopeComponent {
    override val scope: Scope by newScope()

    private val module by lazy { builder(scope) }

    init { loadKoinModules(module) }
}