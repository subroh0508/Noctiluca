package noctiluca.features.timeline

import androidx.compose.runtime.*
import androidx.compose.ui.text.intl.Locale
import cafe.adriel.voyager.core.registry.screenModule
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import noctiluca.features.navigation.Timelines
import noctiluca.features.shared.AuthorizedComposable
import noctiluca.features.timeline.screen.TimelinesNavigationDrawer
import noctiluca.features.timeline.screen.TimelinesScaffold

internal val LocalResources = compositionLocalOf { Resources("JA") }

val featureTimelineScreenModule = screenModule {
    register<Timelines> {
        TimelinesScreen(it.id, it.domain)
    }
}

data class TimelinesScreen(
    private val id: String,
    private val domain: String,
) : Screen {
    override val key: ScreenKey by lazy { "$id@$domain" }

    @Composable
    override fun Content() = AuthorizedComposable(
        LocalResources provides Resources(Locale.current.language),
    ) {
        TimelinesNavigationDrawer { account, domain, drawerState ->
            TimelinesScaffold(
                account,
                domain,
                drawerState,
            )
        }
    }
}
