package noctiluca.features.timeline.template.scaffold

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import noctiluca.features.components.atoms.appbar.NavigateIconSize
import noctiluca.features.components.atoms.appbar.TopAppBar
import noctiluca.features.components.atoms.image.AsyncImage
import noctiluca.features.timeline.getString
import noctiluca.features.timeline.state.CurrentAuthorizedAccount
import noctiluca.features.timeline.state.rememberCurrentAuthorizedAccountStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TimelineScaffold(
    onReload: () -> Unit,
    drawerState: DrawerState,
    bottomBar: @Composable () -> Unit,
    content: @Composable (PaddingValues, TopAppBarScrollBehavior) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val account = rememberCurrentAuthorizedAccountStatus(onReload)

    Scaffold(
        topBar = {
            CurrentInstanceTopAppBar(
                account.value,
                scrollBehavior,
            ) { scope.launch { drawerState.open() } }
        },
        bottomBar = bottomBar,
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { content(it, scrollBehavior) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CurrentInstanceTopAppBar(
    account: CurrentAuthorizedAccount,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    onClickNavigationIcon: () -> Unit,
) = TopAppBar(
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
