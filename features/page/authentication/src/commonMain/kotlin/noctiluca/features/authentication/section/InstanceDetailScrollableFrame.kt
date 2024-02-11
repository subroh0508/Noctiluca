package noctiluca.features.authentication.section

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import noctiluca.features.authentication.component.InstancesTab
import noctiluca.features.authentication.section.scrollableframe.InstanceDetailScrollableFrameState
import noctiluca.features.authentication.section.scrollableframe.InstanceDetailScrollableFrameState.Companion.TAB_INDEX
import noctiluca.features.authentication.section.scrollableframe.rememberInstanceDetailScrollableFrameState
import noctiluca.features.shared.atoms.card.CardHeader
import noctiluca.features.shared.atoms.card.CardSupporting
import noctiluca.features.shared.atoms.card.FilledCard
import noctiluca.features.shared.atoms.snackbar.LocalSnackbarHostState
import noctiluca.features.shared.getCommonString
import noctiluca.features.shared.model.LoadState
import noctiluca.features.shared.utils.description
import noctiluca.features.shared.utils.label

private val HeadlinedScaffoldHorizontalPadding = 16.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun InstanceDetailScrollableFrame(
    tab: InstancesTab,
    tabList: List<InstancesTab>,
    instanceLoadState: LoadState,
    topBar: @Composable (InstanceDetailScrollableFrameState, TopAppBarScrollBehavior) -> Unit,
    tabs: @Composable (InstanceDetailScrollableFrameState) -> Unit,
    bottomBar: @Composable BoxScope.(Dp) -> Unit,
    content: LazyListScope.(
        @Composable (InstanceDetailScrollableFrameState) -> Unit,
        InstanceDetailScrollableFrameState,
        Dp,
    ) -> Unit,
) {
    val scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val scrollableFrameState = rememberInstanceDetailScrollableFrameState(tab, tabList)

    Scaffold(
        topBar = { topBar(scrollableFrameState, scrollBehavior) },
        snackbarHost = { SnackbarHost(LocalSnackbarHostState.current) },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { paddingValues ->
        when (instanceLoadState) {
            is LoadState.Initial,
            is LoadState.Loading -> LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
                    .offset(y = paddingValues.calculateTopPadding()),
            )
            is LoadState.Error -> Fallback(
                instanceLoadState.getErrorOrNull(),
                PaddingValues(
                    top = paddingValues.calculateTopPadding(),
                    start = HeadlinedScaffoldHorizontalPadding,
                    end = HeadlinedScaffoldHorizontalPadding,
                ),
            )
            is LoadState.Loaded -> Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = scrollableFrameState.lazyListState,
                    contentPadding = PaddingValues(
                        start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                        top = paddingValues.calculateTopPadding(),
                        end = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
                        bottom = 64.dp,
                    ),
                ) { content(tabs, scrollableFrameState, HeadlinedScaffoldHorizontalPadding) }

                if (scrollableFrameState.firstVisibleItemIndex >= TAB_INDEX) {
                    Box(
                        modifier = Modifier.offset(y = 64.dp),
                    ) { tabs(scrollableFrameState) }
                }

                bottomBar(HeadlinedScaffoldHorizontalPadding)
            }
        }
    }
}

@Composable
private fun Fallback(
    error: Throwable?,
    paddingValues: PaddingValues,
) {
    error ?: return

    val navigator = LocalNavigator.current

    FilledCard(
        headline = { CardHeader(error.label()) },
        supporting = { CardSupporting(error.description()) },
        actions = {
            Button(
                onClick = { navigator?.pop() },
            ) {
                Text(getCommonString().back)
            }
        },
        modifier = Modifier.padding(
            top = paddingValues.calculateTopPadding(),
            start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
            end = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
        ),
    )
}
