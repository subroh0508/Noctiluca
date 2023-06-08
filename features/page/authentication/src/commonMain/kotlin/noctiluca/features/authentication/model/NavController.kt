package noctiluca.features.authentication.model

import androidx.compose.runtime.compositionLocalOf
import noctiluca.features.authentication.SignInNavigation
import noctiluca.model.Uri

internal val LocalNavController = compositionLocalOf { NavController() }

internal class NavController(
    private val navigation: SignInNavigation? = null,
) {
    fun navigateToTimeline() {
        navigation?.navigateToTimeline()
    }

    fun openBrowser(uri: Uri) {
        navigation?.openBrowser(uri)
    }
}
