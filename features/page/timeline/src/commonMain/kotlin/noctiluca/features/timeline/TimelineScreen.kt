package noctiluca.features.timeline

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.intl.Locale
import noctiluca.components.AuthorizedFeatureComposable
import noctiluca.components.atoms.appbar.TopAppBar
import noctiluca.components.di.getKoinRootScope
import noctiluca.features.timeline.organisms.topappbar.CurrentInstanceTopAppBar
import org.koin.core.component.KoinScopeComponent

internal val LocalResources = compositionLocalOf { Resources("JA") }
internal val LocalScope = compositionLocalOf { getKoinRootScope() }

@Composable
fun TimelineScreen(
    component: KoinScopeComponent,
    onReload: () -> Unit,
    onBackToSignIn: () -> Unit,
) = AuthorizedFeatureComposable(component, onReload, onBackToSignIn) { scope ->
    CompositionLocalProvider(
        LocalResources provides Resources(Locale.current.language),
        LocalScope provides scope,
    ) { TimelineScaffold() }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimelineScaffold() = Scaffold(
    topBar = {
        CurrentInstanceTopAppBar()
    }
) {

}
