package noctiluca.features.timeline

import androidx.compose.runtime.*
import androidx.compose.ui.text.intl.Locale
import cafe.adriel.voyager.core.registry.screenModule
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import noctiluca.features.navigation.Timeline
import noctiluca.features.shared.AuthorizedComposable
import noctiluca.features.timeline.screen.TimelineNavigationDrawer
import noctiluca.features.timeline.screen.TimelineScaffold
import noctiluca.features.timeline.screen.TootScaffold

internal val LocalResources = compositionLocalOf { Resources("JA") }

val featureTimelineScreenModule = screenModule {
    register<Timeline.TimelineLane> {
        TimelineLaneScreen(it.id, it.domain)
    }
    register<Timeline.Toot> {
        TootScreen
    }
}

data class TimelineLaneScreen(
    private val id: String,
    private val domain: String,
) : Screen {
    override val key: ScreenKey by lazy { "$id@$domain" }

    @Composable
    override fun Content() = AuthorizedComposable(
        LocalResources provides Resources(Locale.current.language),
    ) {
        TimelineNavigationDrawer { account, domain, drawerState ->
            TimelineScaffold(
                account,
                domain,
                drawerState,
            )
        }
    }
}

internal data object TootScreen : Screen {
    @Composable
    override fun Content() = AuthorizedComposable(
        LocalResources provides Resources(Locale.current.language),
    ) { TootScaffold() }
}
