package noctiluca.features.timeline

import androidx.compose.runtime.*
import androidx.compose.ui.text.intl.Locale
import cafe.adriel.voyager.core.registry.screenModule
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import noctiluca.features.navigation.Timeline
import noctiluca.features.navigation.navigateToAccountDetail
import noctiluca.features.navigation.navigateToSignIn
import noctiluca.features.navigation.navigateToTimelines
import noctiluca.features.shared.AuthorizedComposable
import noctiluca.features.shared.extensions.getAuthorizedScreenModel
import noctiluca.features.timeline.template.drawer.TimelineNavigationDrawer
import noctiluca.features.timeline.template.drawer.menu.TimelineDrawerMenu
import noctiluca.features.timeline.template.scaffold.TimelineScaffold
import noctiluca.features.timeline.template.scaffold.TootScaffold
import noctiluca.features.timeline.viewmodel.TimelinesViewModel
import noctiluca.model.AuthorizeEventState

internal val LocalResources = compositionLocalOf { Resources("JA") }

val featureTimelineScreenModule = screenModule {
    register<Timeline.TimelineLane> {
        TimelineLaneScreen(it.id, it.domain)
    }
    register<Timeline.Toot> {
        TootScreen
    }
}

data object Splash : Screen {
    @Composable
    override fun Content() {
        AuthorizedComposable(
            LocalResources provides Resources(Locale.current.language),
        ) { context ->
            val state by context.state.collectAsState(AuthorizeEventState())

            if (state.event == AuthorizeEventState.Event.OK) {
                navigateToTimelines(state.user)
            }
        }
    }
}

data class TimelineLaneScreen(
    private val id: String,
    private val domain: String,
) : Screen {
    override val key: ScreenKey by lazy { "$id@$domain" }

    @Composable
    override fun Content() = TimelineFeature { viewModel ->
        val navigator = LocalNavigator.current
        val uiModel by viewModel.uiModel.collectAsState()

        TimelineNavigationDrawer(
            uiModel.account,
            onClickTopAccount = { navigator?.navigateToAccountDetail(it.id) },
            onClickOtherAccount = { account ->
                viewModel.switch(account)
            },
            onClickDrawerMenu = { menu ->
                handleOnClickDrawerItem(
                    menu,
                    navigator,
                )
            },
        ) { drawerState ->
            TimelineScaffold(
                viewModel,
                drawerState,
            )
        }
    }
}

internal data object TootScreen : Screen {
    @Composable
    override fun Content() = TimelineFeature { viewModel ->
        TootScaffold(viewModel)
    }
}

@Composable
private fun Screen.TimelineFeature(
    content: @Composable (TimelinesViewModel) -> Unit,
) = AuthorizedComposable(
    LocalResources provides Resources(Locale.current.language),
) { content(getAuthorizedScreenModel()) }

private fun handleOnClickDrawerItem(
    item: TimelineDrawerMenu,
    navigator: Navigator?,
) = when (item) {
    is TimelineDrawerMenu.NewAccount -> navigator?.navigateToSignIn()
    is TimelineDrawerMenu.Settings -> Unit
}
