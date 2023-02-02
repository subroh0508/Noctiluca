package noctiluca.features.timeline

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import noctiluca.features.components.AuthorizedFeatureComposable
import noctiluca.features.components.atoms.appbar.scrollToTop
import noctiluca.features.components.di.getKoinRootScope
import noctiluca.features.shared.account.AccountHeader
import noctiluca.features.shared.status.Action
import noctiluca.features.timeline.organisms.list.TimelineLane
import noctiluca.features.timeline.organisms.navigationbar.TimelineNavigationBar
import noctiluca.features.timeline.organisms.scaffold.TimelineScaffold
import noctiluca.features.timeline.state.CurrentAuthorizedAccount
import noctiluca.features.timeline.state.TimelineListState
import noctiluca.features.timeline.state.rememberTimelineStatus
import org.koin.core.component.KoinScopeComponent

internal val LocalResources = compositionLocalOf { Resources("JA") }
internal val LocalScope = compositionLocalOf { getKoinRootScope() }
internal val LocalTimelineListState = compositionLocalOf { TimelineListState() }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimelineScreen(
    component: KoinScopeComponent,
    onReload: () -> Unit,
    onBackToSignIn: () -> Unit,
) = AuthorizedFeatureComposable(component, onReload, onBackToSignIn) { scope ->
    val timeline = rememberTimelineStatus(scope)

    CompositionLocalProvider(
        LocalResources provides Resources(Locale.current.language),
        LocalScope provides scope,
        LocalTimelineListState provides timeline,
    ) {
        TimelineScaffold(
            drawerContent = { scope, drawerState, account ->
                DrawerSheet(account) { scope.launch { drawerState.close() } }
            },
            bottomBar = { TimelineNavigationBar() },
        ) { paddingValues, scrollBehavior -> TimelineLanes(paddingValues, scrollBehavior) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DrawerSheet(
    account: CurrentAuthorizedAccount,
    onClose: () -> Unit,
) = ModalDrawerSheet {
    Spacer(Modifier.height(12.dp))
    account.current?.let {
        AccountHeader(
            it,
            onClickAccountIcon = {},
            modifier = Modifier.padding(16.dp),
        )
    }

    NavigationDrawerItem(
        icon = { Icon(Icons.Default.BrokenImage, contentDescription = null) },
        label = { Text("Test") },
        selected = false,
        onClick = { onClose() },
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
    )
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
