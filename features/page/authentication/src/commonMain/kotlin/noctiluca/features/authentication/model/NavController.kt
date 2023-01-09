package noctiluca.features.authentication.model

import androidx.compose.runtime.compositionLocalOf
import noctiluca.features.components.utils.Browser
import noctiluca.model.Uri

internal val LocalNavController = compositionLocalOf { NavController() }

internal class NavController(
    private val onNavigateToTimeline: () -> Unit = {},
    private val browser: Browser? = null,
) {
    fun navigateToTimeline() = onNavigateToTimeline()
    fun openBrowser(uri: Uri) { browser?.open(uri) }
}
