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
import org.koin.core.component.KoinScopeComponent

internal val LocalNavigation = compositionLocalOf<TimelinesNavigator?> { null }
internal val LocalResources = compositionLocalOf { Resources("JA") }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimelineScreen(
    component: KoinScopeComponent,
    navigation: TimelineNavigation,
) = TimelineFeature(
    component,
    navigation,
) { page ->
    when (page) {
        is TimelinesNavigator.Child.Timelines -> {
            val viewModel = TimelinesViewModel.Provider(
                page,
                reload = { navigation.reopenApp() },
            )

            val uiModel by viewModel.uiModel.subscribeAsState()

            LaunchedEffect(Unit) {
                viewModel.loadCurrentAuthorizedAccount()
            }

            TimelineNavigationDrawer(
                uiModel.account,
                onClickTopAccount = { navigation.navigateToAccountDetail(it.id.value) },
                onClickOtherAccount = { account ->
                    viewModel.switch(account)
                },
                onClickDrawerMenu = { menu ->
                    handleOnClickDrawerItem(
                        menu,
                        navigation,
                    )
                },
            ) { drawerState ->
                TimelineScaffold(
                    viewModel,
                    drawerState,
                )
            }
        }

        is TimelinesNavigator.Child.Toot -> TootScaffold(
            TimelinesViewModel.Provider(
                page,
                reload = { navigation.reopenApp() },
            ),
        )

        is TimelinesNavigator.Child.AccountDetail -> Unit
    }
}

@Composable
private fun TimelineFeature(
    component: KoinScopeComponent,
    navigation: TimelineNavigation,
    content: @Composable (TimelinesNavigator.Child) -> Unit,
) = AuthorizedFeatureComposable(
    context = TimelinesNavigator(),
    navigation,
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
    navigation: TimelineNavigation,
) = when (item) {
    is TimelineDrawerMenu.NewAccount -> navigation.backToSignIn()
    is TimelineDrawerMenu.Settings -> Unit
}
