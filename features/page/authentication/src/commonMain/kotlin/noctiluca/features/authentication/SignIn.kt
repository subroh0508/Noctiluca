package noctiluca.features.authentication

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import noctiluca.components.FeatureComponent
import noctiluca.components.atoms.appbar.TopAppBar
import noctiluca.components.di.getKoin
import noctiluca.features.authentication.model.AuthorizeCode
import noctiluca.features.authentication.templates.SelectInstanceForm
import org.koin.core.component.KoinScopeComponent

internal val LocalResources = compositionLocalOf { Resources("JA") }
internal val LocalScope = compositionLocalOf { getKoin().getScope("_root_") }

@Composable
fun SignIn(
    authorizeState: State<AuthorizeCode?>,
    koinComponent: KoinScopeComponent,
) = FeatureComponent(koinComponent) {
    CompositionLocalProvider(
        LocalResources provides Resources(Locale.current.language),
        LocalScope provides koinComponent.scope,
    ) { SignInScaffold(authorizeState) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignInScaffold(
    authorizeState: State<AuthorizeCode?>,
) = Scaffold(
    topBar = {
        TopAppBar(getString().sign_in_page_title)
    },
) { paddingValues ->
    SelectInstanceForm(
        authorizeState,
        modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
    )
}
