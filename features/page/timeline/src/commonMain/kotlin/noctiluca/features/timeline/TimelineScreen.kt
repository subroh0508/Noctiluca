package noctiluca.features.timeline

import androidx.compose.runtime.*
import androidx.compose.ui.text.intl.Locale
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.registry.screenModule
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import noctiluca.features.navigation.SignInScreen
import noctiluca.features.navigation.TimelineScreen
import noctiluca.features.shared.AuthorizedComposable
import noctiluca.features.timeline.di.TimelineComponent
import noctiluca.features.timeline.template.drawer.TimelineNavigationDrawer
import noctiluca.features.timeline.template.drawer.menu.TimelineDrawerMenu
import noctiluca.features.timeline.template.scaffold.TimelineScaffold
import noctiluca.features.timeline.template.scaffold.TootScaffold
import noctiluca.features.timeline.viewmodel.TimelinesViewModel

internal val LocalResources = compositionLocalOf { Resources("JA") }

val featureTimelineScreenModule = screenModule {
    register<TimelineScreen.Timelines> {
        TimelinesScreen
    }
    register<TimelineScreen.Toot> {
        TootScreen
    }
}

data object TimelinesScreen : Screen {
    @Composable
    override fun Content() = TimelineFeature { viewModel ->
        val navigator = LocalNavigator.current
        val uiModel by viewModel.uiModel.subscribeAsState()

        val signInScreen = rememberScreen(SignInScreen.MastodonInstanceList)

        LaunchedEffect(uiModel.account.current) {
            viewModel.loadCurrentAuthorizedAccount()
        }

        TimelineNavigationDrawer(
            uiModel.account,
            onClickTopAccount = { /*screen.navigateToAccountDetail(it.id.value)*/ },
            onClickOtherAccount = { account ->
                viewModel.switch(account)
            },
            onClickDrawerMenu = { menu ->
                handleOnClickDrawerItem(
                    menu,
                    navigator,
                    signInScreen,
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

data object TootScreen : Screen {
    @Composable
    override fun Content() = TimelineFeature { viewModel ->
        TootScaffold(viewModel)
    }
}

@Composable
private fun TimelineFeature(
    content: @Composable (TimelinesViewModel) -> Unit,
) {
    val component = remember { TimelineComponent() }
    val viewModel = TimelinesViewModel.Provider(component)

    AuthorizedComposable(viewModel) {
        CompositionLocalProvider(
            LocalResources provides Resources(Locale.current.language),
        ) { content(viewModel) }
    }
}

private fun handleOnClickDrawerItem(
    item: TimelineDrawerMenu,
    navigator: Navigator?,
    screen: Screen,
) = when (item) {
    is TimelineDrawerMenu.NewAccount -> navigator?.push(screen)
    is TimelineDrawerMenu.Settings -> Unit
}
