package noctiluca.features.timeline

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.intl.Locale
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import noctiluca.features.components.AuthorizedFeatureComposable
import noctiluca.features.timeline.template.drawer.TimelineNavigationDrawer
import noctiluca.features.timeline.template.drawer.menu.TimelineDrawerMenu
import noctiluca.features.timeline.template.scaffold.TimelineScaffold
import noctiluca.features.timeline.template.scaffold.TootScaffold
import noctiluca.features.timeline.viewmodel.TimelinesViewModel

internal val LocalNavigation = compositionLocalOf<TimelineNavigator.Screen?> { null }
internal val LocalResources = compositionLocalOf { Resources("JA") }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimelineScreen(
    screen: TimelineNavigator.Screen,
) = TimelineFeature(
    screen,
) { page ->
    when (page) {
        is TimelineNavigator.Screen.Child.Timelines -> {
            val viewModel = TimelinesViewModel.Provider(
                screen,
                reload = { screen.reopenApp() },
            )

            val uiModel by viewModel.uiModel.subscribeAsState()

            LaunchedEffect(Unit) {
                viewModel.loadCurrentAuthorizedAccount()
            }

            TimelineNavigationDrawer(
                uiModel.account,
                onClickTopAccount = { screen.navigateToAccountDetail(it.id.value) },
                onClickOtherAccount = { account ->
                    viewModel.switch(account)
                },
                onClickDrawerMenu = { menu ->
                    handleOnClickDrawerItem(
                        menu,
                        screen,
                    )
                },
            ) { drawerState ->
                TimelineScaffold(
                    viewModel,
                    drawerState,
                )
            }
        }

        is TimelineNavigator.Screen.Child.Toot -> TootScaffold(
            TimelinesViewModel.Provider(
                screen,
                reload = { screen.reopenApp() },
            ),
        )
    }
}

@Composable
private fun TimelineFeature(
    screen: TimelineNavigator.Screen,
    content: @Composable (TimelineNavigator.Screen.Child) -> Unit,
) = AuthorizedFeatureComposable(
    context = screen,
    navigator = screen,
) { navigator ->
    CompositionLocalProvider(
        LocalResources provides Resources(Locale.current.language),
        LocalNavigation provides navigator,
    ) {
        val page by navigator.childStack.subscribeAsState()

        content(page.active.instance)
    }
}

private fun handleOnClickDrawerItem(
    item: TimelineDrawerMenu,
    screen: TimelineNavigator.Screen,
) = when (item) {
    is TimelineDrawerMenu.NewAccount -> screen.backToSignIn()
    is TimelineDrawerMenu.Settings -> Unit
}
