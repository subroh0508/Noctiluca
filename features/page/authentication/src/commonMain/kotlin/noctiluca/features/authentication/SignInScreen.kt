package noctiluca.features.authentication

import androidx.compose.runtime.*
import androidx.compose.ui.text.intl.Locale
import noctiluca.features.authentication.model.AuthorizeResult
import noctiluca.features.authentication.model.LocalNavController
import noctiluca.features.authentication.model.NavController
import noctiluca.features.authentication.templates.scaffold.SearchInstanceScaffold
import noctiluca.features.components.FeatureComposable
import noctiluca.features.components.di.getKoinRootScope
import org.koin.core.component.KoinScopeComponent

internal val LocalResources = compositionLocalOf { Resources("JA") }
internal val LocalScope = compositionLocalOf { getKoinRootScope() }
internal val LocalAuthorizeResult = compositionLocalOf<AuthorizeResult?> { null }

@Composable
fun SignInScreen(
    authorizeResult: AuthorizeResult?,
    koinComponent: KoinScopeComponent,
    onNavigateToTimeline: () -> Unit,
) = FeatureComposable(koinComponent) { scope ->
    CompositionLocalProvider(
        LocalResources provides Resources(Locale.current.language),
        LocalScope provides scope,
        LocalAuthorizeResult provides authorizeResult,
        LocalNavController provides NavController(
            onNavigateToTimeline = onNavigateToTimeline,
            browser = scope.get(),
        ),
    ) { SignInScaffold() }
}

@Composable
private fun SignInScaffold() = SearchInstanceScaffold(
    onSelect = { _ -> },
)
