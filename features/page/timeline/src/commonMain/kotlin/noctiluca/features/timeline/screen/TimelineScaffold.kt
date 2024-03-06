package noctiluca.features.timeline.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import kotlinx.coroutines.launch
import noctiluca.features.navigation.navigateToAccountDetail
import noctiluca.features.navigation.navigateToStatusDetail
import noctiluca.features.shared.atoms.appbar.scrollToTop
import noctiluca.features.shared.extensions.getAuthorizedScreenModel
import noctiluca.features.shared.model.LoadState
import noctiluca.features.shared.molecules.scaffold.TabbedScaffold
import noctiluca.features.shared.status.Action
import noctiluca.features.timeline.TimelinesScreen
import noctiluca.features.timeline.model.TimelinesModel
import noctiluca.features.timeline.organisms.card.TootCard
import noctiluca.features.timeline.organisms.list.TimelineLane
import noctiluca.features.timeline.organisms.tab.TimelineTabs
import noctiluca.features.timeline.section.TimelinesTopAppBar
import noctiluca.features.timeline.viewmodel.TimelinesViewModel
import noctiluca.model.Domain
import noctiluca.model.account.Account
import noctiluca.model.timeline.TimelineId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TimelinesScreen.TimelinesScaffold(
    current: Account?,
    domain: Domain?,
    drawerState: DrawerState,
) {
    val viewModel: TimelinesViewModel = getAuthorizedScreenModel()

    val uiModel by viewModel.uiModel.collectAsState()

    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val lazyListState = remember(
        current,
        uiModel.tabs.keys,
    ) { uiModel.tabs.mapValues { (_, _) -> LazyListState() } }

    TabbedScaffold(
        scrollBehavior,
        topAppBar = {
            TimelinesTopAppBar(
                current?.avatar,
                domain,
                scrollBehavior,
                onClickNavigationIcon = {
                    scope.launch { drawerState.open() }
                },
            )
        },
        bottomBar = {
            TootCard(
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter)
            )
        },
        tabs = {
            TimelineTabs(
                uiModel,
                onClickTab = { timelineId ->
                    if (uiModel.tabs[timelineId]?.foreground == true) {
                        scope.launch { lazyListState[timelineId]?.animateScrollToItem(0) }
                        scrollBehavior.scrollToTop()
                    }

                    viewModel.setForeground(timelineId)
                },
            )
        },
    ) {
        TimelineLanes(
            viewModel,
            uiModel.tabs,
            uiModel.loadState,
            lazyListState,
        )
    }
}

@Composable
private fun TimelineLanes(
    viewModel: TimelinesViewModel,
    timelines: Map<TimelineId, TimelinesModel.State>,
    loadState: Map<TimelineId, LoadState>,
    lazyListState: Map<TimelineId, LazyListState>,
) {
    val navigator = LocalNavigator.current

    timelines.forEach { (timelineId, timelineState) ->
        TimelineLane(
            timelineState,
            loadState[timelineId],
            lazyListState = lazyListState[timelineId] ?: rememberLazyListState(),
            onLoad = { viewModel.load(timelineId) },
            onClickStatus = { navigator?.navigateToStatusDetail(it) },
            onClickAvatar = { navigator?.navigateToAccountDetail(it) },
            onExecuteAction = { _, status, action ->
                when (action) {
                    Action.FAVOURITE -> viewModel.favourite(status)
                    Action.BOOST -> viewModel.boost(status)
                    else -> Unit
                }
            },
        )
    }
}
