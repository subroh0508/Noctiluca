package noctiluca.features.authentication.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import noctiluca.model.Uri

internal val LocalNavController = compositionLocalOf { NavController() }

internal class NavController(
    private val onNavigateToTimeline: () -> Unit = {},
    private val onOpenBrowser: @Composable (Uri) -> Unit = {},
) {
    fun navigateToTimeline() = onNavigateToTimeline()

    @Composable
    fun openBrowser(uri: Uri) = onOpenBrowser(uri)
}
