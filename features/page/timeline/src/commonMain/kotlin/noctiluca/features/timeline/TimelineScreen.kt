package noctiluca.features.timeline

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import noctiluca.features.components.AuthorizedFeatureComposable
import noctiluca.features.components.atoms.appbar.scrollToTop
import noctiluca.features.components.di.getKoinRootScope
import noctiluca.features.shared.status.Action
import noctiluca.features.timeline.organisms.list.TimelineLane
import noctiluca.features.timeline.organisms.navigationbar.TimelineNavigationBar
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
) = AuthorizedFeatureComposable(component, onReload, onBackToSignIn) { scope ->
    val timeline = rememberTimelineStatus(scope)

    CompositionLocalProvider(
        LocalResources provides Resources(Locale.current.language),
        LocalScope provides scope,
        LocalTimelineListState provides timeline,
    ) {
        TimelineNavigationDrawer(
            rememberCurrentAuthorizedAccountStatus(onReload),
            onClickTopAccount = { onNavigateToAccountDetail(it.id.value) },
            onClickDrawerMenu = { menu ->
                handleOnClickDrawerItem(
                    menu,
                    onBackToSignIn,
                )
            },
        ) {
            TimelineScaffold(
                onReload,
                bottomBar = { TimelineNavigationBar() },
            ) { paddingValues, scrollBehavior ->
                TimelineLanes(
                    paddingValues,
                    scrollBehavior,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimelineLanes(
    paddingValues: PaddingValues,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    val timelineListState = LocalTimelineListState.current

    timelineListState.value.forEachIndexed { index, timelineState ->
        TimelineLane(
            timelineState,
            onLoad = { timelineListState.load(this, it) },
            onExecuteAction = { timeline, status, action ->
                when (action) {
                    Action.FAVOURITE -> timelineListState.favourite(this, timeline, status)
                    Action.BOOST -> timelineListState.boost(this, timeline, status)
                    else -> Unit
                }
            },
            onScrollToTop = {
                timelineListState.scrolledToTop(index)
                scrollBehavior.scrollToTop()
            },
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
            ),
        )
    }
}

private fun handleOnClickDrawerItem(
    item: TimelineDrawerMenu,
    onBackToSignIn: () -> Unit,
) = when (item) {
    is TimelineDrawerMenu.NewAccount -> onBackToSignIn()
    is TimelineDrawerMenu.Settings -> Unit
}
