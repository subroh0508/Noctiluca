package noctiluca.features.timeline.template.scaffold

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import kotlinx.coroutines.launch
import noctiluca.features.navigation.navigateToAccountDetail
import noctiluca.features.navigation.navigateToStatusDetail
import noctiluca.features.shared.atoms.appbar.NavigateIconSize
import noctiluca.features.shared.atoms.appbar.scrollToTop
import noctiluca.features.shared.atoms.image.AsyncImage
import noctiluca.features.shared.model.LoadState
import noctiluca.features.shared.molecules.scaffold.TabbedScaffold
import noctiluca.features.shared.status.Action
import noctiluca.features.timeline.getString
import noctiluca.features.timeline.model.CurrentAuthorizedAccount
import noctiluca.features.timeline.organisms.card.TootCard
import noctiluca.features.timeline.organisms.list.TimelineLane
import noctiluca.features.timeline.organisms.tab.TimelineTabs
import noctiluca.features.timeline.viewmodel.TimelinesViewModel
import noctiluca.model.timeline.TimelineId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TimelineScaffold(
    viewModel: TimelinesViewModel,
    drawerState: DrawerState,
) {
    val uiModel by viewModel.uiModel.collectAsState()

    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val lazyListState = remember(
        uiModel.account.current,
        uiModel.timelines.keys,
    ) { uiModel.timelines.mapValues { (_, _) -> LazyListState() } }

    TabbedScaffold(
        scrollBehavior,
        topAppBar = {
            CurrentInstanceTopAppBar(
                uiModel.account,
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
                    if (uiModel.timelines[timelineId]?.foreground == true) {
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
            uiModel.timelines,
            uiModel.loadState,
            lazyListState,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CurrentInstanceTopAppBar(
    account: CurrentAuthorizedAccount,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    onClickNavigationIcon: () -> Unit,
) = CenterAlignedTopAppBar(
    { Text(account.domain?.value ?: getString().timeline_page_title) },
    navigationIcon = {
        IconButton(
            onClick = onClickNavigationIcon,
            modifier = Modifier.padding(start = 8.dp),
        ) {
            AsyncImage(
                account.current?.avatar,
                // fallback = imageResources(getDrawables().icon_mastodon),
                modifier = Modifier.size(NavigateIconSize)
                    .clip(RoundedCornerShape(8.dp)),
            )
        }
    },
    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.surface,
        scrolledContainerColor = MaterialTheme.colorScheme.surface,
    ),
    scrollBehavior = topAppBarScrollBehavior,
)

@Composable
private fun TimelineLanes(
    viewModel: TimelinesViewModel,
    timelines: Map<TimelineId, TimelinesViewModel.TimelineState>,
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
