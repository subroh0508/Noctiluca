package noctiluca.components

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.getScopeOrNull
import org.koin.core.scope.Scope

@Composable
fun FeatureComponent(
    component: KoinScopeComponent,
    modifier: Modifier = Modifier,
    content: @Composable (Scope) -> Unit,
) {
    DisposableEffect(component) {
        onDispose { component.closeScope() }
    }

    component.getScopeOrNull()?.let { content(it) }
}
