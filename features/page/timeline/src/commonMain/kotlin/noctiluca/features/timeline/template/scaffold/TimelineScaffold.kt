package noctiluca.features.timeline.template.scaffold

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import noctiluca.features.components.atoms.appbar.CenterAlignedTopAppBar
import noctiluca.features.components.atoms.appbar.NavigateIconSize
import noctiluca.features.components.atoms.appbar.scrollToTop
import noctiluca.features.components.atoms.image.AsyncImage
import noctiluca.features.components.molecules.scaffold.TabbedScaffold
import noctiluca.features.shared.status.Action
import noctiluca.features.timeline.LocalNavigation
import noctiluca.features.timeline.LocalTimelineListState
import noctiluca.features.timeline.getString
import noctiluca.features.timeline.organisms.card.TootCard
import noctiluca.features.timeline.organisms.list.TimelineLane
import noctiluca.features.timeline.organisms.tab.TimelineTabs
import noctiluca.features.timeline.state.CurrentAuthorizedAccount
import noctiluca.features.timeline.state.rememberCurrentAuthorizedAccountStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TimelineScaffold(drawerState: DrawerState) {
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val account = rememberCurrentAuthorizedAccountStatus(LocalNavigation.current)

    TabbedScaffold(
        scrollBehavior,
        topAppBar = {
            CurrentInstanceTopAppBar(
                account.value,
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
        tabs = { TimelineTabs() },
    ) {
        TimelineLanes(scrollBehavior)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CurrentInstanceTopAppBar(
    account: CurrentAuthorizedAccount,
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
        )
    }
}
