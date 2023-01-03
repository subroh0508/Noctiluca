package noctiluca.components

import androidx.compose.runtime.*
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.getScopeOrNull
import org.koin.core.scope.Scope

@Composable
fun FeatureComposable(
    component: KoinScopeComponent,
    content: @Composable (Scope) -> Unit,
) {
    DisposableEffect(component) {
        onDispose { component.closeScope() }
    }

    content(component.scope)
}
