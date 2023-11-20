package noctiluca.features.timeline

import androidx.compose.runtime.*
import androidx.compose.ui.text.intl.Locale
import cafe.adriel.voyager.core.registry.screenModule
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import noctiluca.features.navigation.Timeline
import noctiluca.features.navigation.navigateToAccountDetail
import noctiluca.features.navigation.navigateToSignIn
import noctiluca.features.shared.AuthorizedComposable
import noctiluca.features.timeline.template.drawer.TimelineNavigationDrawer
import noctiluca.features.timeline.template.drawer.menu.TimelineDrawerMenu
import noctiluca.features.timeline.template.scaffold.TimelineScaffold
import noctiluca.features.timeline.template.scaffold.TootScaffold
import noctiluca.features.timeline.viewmodel.TimelinesViewModel

internal val LocalResources = compositionLocalOf { Resources("JA") }

val featureTimelineScreenModule = screenModule {
    register<Timeline.TimelineLane> {
        TimelineLaneScreen
    }
    register<Timeline.Toot> {
        TootScreen
    }
}

data object TimelineLaneScreen : Screen {
    @Composable
    override fun Content() = TimelineFeature { viewModel ->
        val navigator = LocalNavigator.current
        val uiModel by viewModel.uiModel.collectAsState()

        LaunchedEffect(uiModel.account.current) {
            viewModel.loadCurrentAuthorizedAccount()
        }

        TimelineNavigationDrawer(
            uiModel.account,
            onClickTopAccount = { navigator?.navigateToAccountDetail(it.id) },
            onClickOtherAccount = { account ->
                viewModel.switch(account)
            },
            onClickDrawerMenu = { menu ->
                handleOnClickDrawerItem(
                    menu,
                    viewModel,
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
) {
    val viewModel: TimelinesViewModel = getScreenModel()

    AuthorizedComposable(
        viewModel,
        LocalResources provides Resources(Locale.current.language),
    ) { content(viewModel) }
}

private fun handleOnClickDrawerItem(
    item: TimelineDrawerMenu,
    viewModel: TimelinesViewModel,
    navigator: Navigator?,
) = when (item) {
    is TimelineDrawerMenu.NewAccount -> {
        viewModel.clear()
        navigator?.navigateToSignIn()
    }
    is TimelineDrawerMenu.Settings -> Unit
}
