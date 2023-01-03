package noctiluca.features.timeline

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import noctiluca.components.AuthorizedFeatureComposable
import org.koin.core.component.KoinScopeComponent

@Composable
fun TimelineScreen(
    component: KoinScopeComponent,
    onReload: () -> Unit,
    onBackToSignIn: () -> Unit,
) = AuthorizedFeatureComposable(component, onReload, onBackToSignIn) {
    Text(
        "Timeline",
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.titleLarge,
    )
}