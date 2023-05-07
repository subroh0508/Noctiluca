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
    onNavigateToAccountDetail: (String) -> Unit,
    onReload: () -> Unit,
    onBackToSignIn: () -> Unit,
) = TimelineFeature(component, onReload, onBackToSignIn) {
    TimelineNavigationDrawer(
        rememberCurrentAuthorizedAccountStatus(onReload),
        onClickTopAccount = { onNavigateToAccountDetail(it.id.value) },
        onClickDrawerMenu = { menu ->
            handleOnClickDrawerItem(
                menu,
                onBackToSignIn,
            )
        },
    ) { drawerState ->
        TimelineScaffold(
            onReload,
            drawerState,
        )
    }
}

@Composable
private fun TimelineFeature(
    component: KoinScopeComponent,
    onReload: () -> Unit,
    onBackToSignIn: () -> Unit,
    content: @Composable () -> Unit,
) = AuthorizedFeatureComposable(component, onReload, onBackToSignIn) { scope ->
    CompositionLocalProvider(
        LocalResources provides Resources(Locale.current.language),
        LocalScope provides scope,
        LocalTimelineListState provides rememberTimelineStatus(scope),
    ) { content() }
}

private fun handleOnClickDrawerItem(
    item: TimelineDrawerMenu,
    onBackToSignIn: () -> Unit,
) = when (item) {
    is TimelineDrawerMenu.NewAccount -> onBackToSignIn()
    is TimelineDrawerMenu.Settings -> Unit
}
