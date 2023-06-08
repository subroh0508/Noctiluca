package noctiluca.features.timeline

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.intl.Locale
import noctiluca.features.components.AuthorizedFeatureComposable
import noctiluca.features.components.di.getKoinRootScope
import noctiluca.features.timeline.state.TimelineListState
import noctiluca.features.timeline.state.rememberCurrentAuthorizedAccountStatus
import noctiluca.features.timeline.state.rememberTimelineStatus
import noctiluca.features.timeline.template.drawer.TimelineNavigationDrawer
import noctiluca.features.timeline.template.drawer.menu.TimelineDrawerMenu
import noctiluca.features.timeline.template.scaffold.TimelineScaffold
import org.koin.core.component.KoinScopeComponent

internal val LocalResources = compositionLocalOf { Resources("JA") }
internal val LocalScope = compositionLocalOf { getKoinRootScope() }
internal val LocalTimelineListState = compositionLocalOf { TimelineListState() }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimelineScreen(
    component: KoinScopeComponent,
    navigation: TimelineNavigation,
) = TimelineFeature(component, navigation) {
    TimelineNavigationDrawer(
        rememberCurrentAuthorizedAccountStatus(navigation),
        onClickTopAccount = { navigation.navigateToAccountDetail(it.id.value) },
        onClickDrawerMenu = { menu ->
            handleOnClickDrawerItem(
                menu,
                navigation,
            )
        },
    ) { drawerState ->
        TimelineScaffold(
            navigation,
            drawerState,
        )
    }
}

@Composable
private fun TimelineFeature(
    component: KoinScopeComponent,
    navigation: TimelineNavigation,
    content: @Composable () -> Unit,
) = AuthorizedFeatureComposable(component, navigation) { scope ->
    CompositionLocalProvider(
        LocalResources provides Resources(Locale.current.language),
        LocalScope provides scope,
        LocalTimelineListState provides rememberTimelineStatus(scope),
    ) { content() }
}

private fun handleOnClickDrawerItem(
    item: TimelineDrawerMenu,
    navigation: TimelineNavigation,
) = when (item) {
    is TimelineDrawerMenu.NewAccount -> navigation.backToSignIn()
    is TimelineDrawerMenu.Settings -> Unit
}
