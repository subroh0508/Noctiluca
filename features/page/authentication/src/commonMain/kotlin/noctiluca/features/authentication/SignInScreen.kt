package noctiluca.features.authentication

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import noctiluca.components.FeatureComposable
import noctiluca.components.atoms.appbar.TopAppBar
import noctiluca.components.di.getKoin
import noctiluca.components.utils.openBrowser
import noctiluca.features.authentication.model.AuthorizeCode
import noctiluca.features.authentication.model.LocalNavController
import noctiluca.features.authentication.model.NavController
import noctiluca.features.authentication.templates.SelectInstanceForm
import org.koin.core.component.KoinScopeComponent

internal val LocalResources = compositionLocalOf { Resources("JA") }
internal val LocalScope = compositionLocalOf { getKoin().getScope("_root_") }
internal val LocalAuthorizeCode = compositionLocalOf<AuthorizeCode?> { null }

@Composable
fun SignInScreen(
    authorizeCode: AuthorizeCode?,
    koinComponent: KoinScopeComponent,
    onNavigateToTimeline: () -> Unit,
) = FeatureComposable(koinComponent) {
    CompositionLocalProvider(
        LocalResources provides Resources(Locale.current.language),
        LocalScope provides koinComponent.scope,
        LocalAuthorizeCode provides authorizeCode,
        LocalNavController provides NavController(
            onNavigateToTimeline = onNavigateToTimeline,
            onOpenBrowser = { openBrowser(it) },
        ),
    ) { SignInScaffold() }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignInScaffold() = Scaffold(
    topBar = {
        TopAppBar(getString().sign_in_page_title)
    },
) { paddingValues ->
    SelectInstanceForm(Modifier.padding(top = paddingValues.calculateTopPadding()))
}
