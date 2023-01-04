package noctiluca.features.authentication

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import noctiluca.components.FeatureComposable
import noctiluca.components.atoms.appbar.TopAppBar
import noctiluca.components.atoms.snackbar.LocalSnackbarHostState
import noctiluca.components.di.getKoinRootScope
import noctiluca.features.authentication.model.AuthorizeResult
import noctiluca.features.authentication.model.LocalNavController
import noctiluca.features.authentication.model.NavController
import noctiluca.features.authentication.templates.SelectInstanceForm
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
    val snackbarHostState = remember { SnackbarHostState() }

    CompositionLocalProvider(
        LocalResources provides Resources(Locale.current.language),
        LocalScope provides scope,
        LocalAuthorizeResult provides authorizeResult,
        LocalNavController provides NavController(
            onNavigateToTimeline = onNavigateToTimeline,
            browser = scope.get(),
        ),
        LocalSnackbarHostState provides snackbarHostState,
    ) { SignInScaffold(snackbarHostState) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignInScaffold(
    snackbarHostState: SnackbarHostState,
) = Scaffold(
    topBar = {
        TopAppBar(getString().sign_in_page_title)
    },
    snackbarHost = { SnackbarHost(snackbarHostState) },
) { paddingValues ->
    SelectInstanceForm(Modifier.padding(top = paddingValues.calculateTopPadding()))
}
