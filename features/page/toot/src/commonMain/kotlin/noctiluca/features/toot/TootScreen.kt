package noctiluca.features.toot

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.intl.Locale
import cafe.adriel.voyager.core.registry.screenModule
import cafe.adriel.voyager.core.screen.Screen
import noctiluca.features.navigation.Toot
import noctiluca.features.shared.AuthorizedComposable
import noctiluca.features.toot.screen.TootScaffold

internal val LocalResources = compositionLocalOf { Resources("JA") }

val featureTootScreenModule = screenModule {
    register<Toot> {
        TootScreen
    }
}

internal data object TootScreen : Screen {
    @Composable
    override fun Content() = AuthorizedComposable(
        LocalResources provides Resources(Locale.current.language),
    ) { TootScaffold() }
}
