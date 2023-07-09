package noctiluca.features.components

import androidx.compose.runtime.*
import androidx.compose.ui.text.intl.Locale
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import org.koin.core.component.KoinScopeComponent
import org.koin.core.scope.Scope

@Composable
fun FeatureComposable(
    component: KoinScopeComponent,
    content: @Composable (Scope) -> Unit,
) {
    DisposableEffect(component) {
        onDispose { component.closeScope() }
    }

    CompositionLocalProvider(
        LocalCommonResources provides Resources(Locale.current.language),
    ) { content(component.scope) }
}

@Composable
fun <T : Navigator.Screen> FeatureComposable(
    context: T,
    content: @Composable (T) -> Unit,
) {
    DisposableEffect(context) {
        context.resume()
        onDispose { context.destroy() }
    }

    CompositionLocalProvider(
        LocalCommonResources provides Resources(Locale.current.language),
    ) { content(context) }
}
