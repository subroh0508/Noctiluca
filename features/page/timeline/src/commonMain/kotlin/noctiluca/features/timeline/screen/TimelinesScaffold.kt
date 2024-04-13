package noctiluca.features.timeline.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import noctiluca.features.shared.extensions.getAuthorizedScreenModel
import noctiluca.features.shared.status.Action
import noctiluca.features.timeline.TimelinesScreen
import noctiluca.features.timeline.component.TimelineTabs
import noctiluca.features.timeline.model.TimelinesModel
import noctiluca.features.timeline.section.TimelineContent
import noctiluca.features.timeline.section.TimelinesScrollableFrame
import noctiluca.features.timeline.section.TimelinesTopAppBar
import noctiluca.features.timeline.viewmodel.TimelinesViewModel
import noctiluca.features.toot.screen.TootCard
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

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val lazyListState = remember(
        current,
        uiModel.tabs.keys,
    ) { uiModel.tabs.mapValues { (_, _) -> LazyListState() } }

    TimelinesScrollableFrame(
        scrollBehavior,
        topAppBar = {
            TimelinesTopAppBar(
                current?.avatar,
                domain,
                scrollBehavior,
                drawerState,
            )
        },
        bottomBar = {
            TootCard(
                getAuthorizedScreenModel(),
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter)
            )
        },
        tabs = {
            TimelineTabs(
                uiModel.tabs,
                uiModel.currentTabIndex,
                lazyListState,
                scrollBehavior,
                onClickTab = { timelineId ->
                    viewModel.setForeground(timelineId)
                },
            )
        },
    ) {
        TimelineTabContent(
            viewModel,
            uiModel,
            lazyListState,
        )
    }
}

@Composable
private fun TimelineTabContent(
    viewModel: TimelinesViewModel,
    uiModel: TimelinesModel,
    lazyListStateMap: Map<TimelineId, LazyListState>,
) {
    val (timelineId, timelineState, loadState) = uiModel.foreground ?: return
    val lazyListState = lazyListStateMap[timelineId] ?: return

    val navigator = LocalNavigator.current

    TimelineContent(
        timelineId,
        timelineState,
        loadState,
        lazyListState,
        onLoad = { viewModel.load(timelineId) },
        onClickStatus = { navigator?.push(it) },
        onClickAvatar = { navigator?.push(it) },
        onExecuteAction = { _, status, action ->
            when (action) {
                Action.FAVOURITE -> viewModel.favourite(status)
                Action.BOOST -> viewModel.boost(status)
                else -> Unit
            }
        },
    )
}
