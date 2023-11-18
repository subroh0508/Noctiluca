package noctiluca.features.timeline.template.scaffold

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import noctiluca.features.shared.atoms.appbar.CenterAlignedTopAppBar
import noctiluca.features.shared.atoms.appbar.NavigateIconSize
import noctiluca.features.shared.atoms.appbar.scrollToTop
import noctiluca.features.shared.atoms.image.AsyncImage
import noctiluca.features.shared.molecules.scaffold.TabbedScaffold
import noctiluca.features.shared.status.Action
import noctiluca.features.timeline.getString
import noctiluca.features.timeline.organisms.card.TootCard
import noctiluca.features.timeline.organisms.list.TimelineLane
import noctiluca.features.timeline.organisms.tab.TimelineTabs
import noctiluca.features.timeline.viewmodel.TimelinesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TimelineScaffold(
    viewModel: TimelinesViewModel,
    drawerState: DrawerState,
) {
    val uiModel by viewModel.uiModel.collectAsState()

    LaunchedEffect(uiModel.account.current) {
        viewModel.subscribeAll()
        viewModel.loadAll()
    }

    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

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
                onClickTab = { viewModel.setForeground(it) },
            )
        },
    ) {
        TimelineLanes(
            viewModel,
            uiModel.timelines,
            scrollBehavior,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CurrentInstanceTopAppBar(
    account: TimelinesViewModel.CurrentAuthorizedAccount,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    onClickNavigationIcon: () -> Unit,
) = CenterAlignedTopAppBar(
    account.domain?.value ?: getString().timeline_page_title,
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
    scrollBehavior = topAppBarScrollBehavior,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimelineLanes(
    viewModel: TimelinesViewModel,
    timelines: List<TimelinesViewModel.TimelineState>,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    timelines.forEachIndexed { index, timelineState ->
        TimelineLane(
            timelineState,
            onLoad = { viewModel.load(it) },
            onExecuteAction = { timeline, status, action ->
                when (action) {
                    Action.FAVOURITE -> viewModel.favourite(timeline, status)
                    Action.BOOST -> viewModel.boost(timeline, status)
                    else -> Unit
                }
            },
            onScrollToTop = {
                viewModel.scrolledToTop(index)
                scrollBehavior.scrollToTop()
            },
        )
    }
}
