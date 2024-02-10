package noctiluca.features.authentication.section

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import noctiluca.features.authentication.component.tab.InstancesTab
import noctiluca.features.authentication.getString
import noctiluca.features.shared.atoms.card.CardHeader
import noctiluca.features.shared.atoms.card.CardSupporting
import noctiluca.features.shared.atoms.card.FilledCard
import noctiluca.features.shared.atoms.snackbar.LocalSnackbarHostState
import noctiluca.features.shared.getCommonString
import noctiluca.features.shared.model.LoadState
import noctiluca.features.shared.utils.description
import noctiluca.features.shared.utils.label
import noctiluca.model.authentication.Instance

private const val TAB_INDEX = 3

private val HeadlinedScaffoldHorizontalPadding = 16.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun InstanceDetailScrollableFrame(
    instance: Instance?,
    instanceLoadState: LoadState,
    topBar: @Composable (InstanceDetailScrollState, TopAppBarScrollBehavior) -> Unit,
    tabs: @Composable (InstanceDetailScrollState) -> Unit,
    bottomBar: @Composable BoxScope.(Dp) -> Unit,
    content: LazyListScope.(@Composable (InstanceDetailScrollState) -> Unit, InstanceDetailScrollState, Dp) -> Unit,
) {
    val scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val scrollableFrameState = rememberTabbedInstanceDetailState(instance)

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
            is LoadState.Loaded<*> -> Box(modifier = Modifier.fillMaxSize()) {
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

                if (scrollableFrameState.lazyListState.firstVisibleItemIndex >= TAB_INDEX) {
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

@Composable
internal fun rememberTabbedInstanceDetailState(
    instance: Instance?,
    initTabIndex: Int = 0,
): InstanceDetailScrollState {
    val scrollState = InstanceDetailScrollState(instance, initTabIndex)

    LaunchedEffect(initTabIndex) {
        val (tab) = scrollState.tabs
            .getOrNull(initTabIndex) ?: return@LaunchedEffect

        scrollState.restoreScrollPosition(tab)
    }
    return scrollState
}

internal class InstanceDetailScrollState private constructor(
    val tabs: List<Pair<InstancesTab, String>>,
    val lazyListState: LazyListState,
    private val currentTabIndex: MutableState<Int>,
    private val scrollPositions: MutableState<List<Pair<Int, Int>>>,
) {
    companion object {
        @Composable
        operator fun invoke(
            instance: Instance?,
            initIndex: Int,
            lazyListState: LazyListState = rememberLazyListState(),
        ): InstanceDetailScrollState {
            val tabTitles = buildTabTitles(instance)

            return remember(instance) {
                InstanceDetailScrollState(
                    tabTitles,
                    lazyListState,
                    mutableStateOf(initIndex),
                    mutableStateOf(List(tabTitles.size) { 1 to 0 }),
                )
            }
        }

        @Composable
        private fun buildTabTitles(
            instance: Instance?,
        ): List<Pair<InstancesTab, String>> {
            instance ?: return listOf()

            @Suppress("MagicNumber")
            return listOfNotNull(
                InstancesTab.INFO to getString().sign_in_instance_detail_tab_info,
                if ((instance.version?.major ?: 0) >= 4) {
                    InstancesTab.EXTENDED_DESCRIPTION to getString().sign_in_instance_detail_tab_extended_description
                } else {
                    null
                },
                InstancesTab.LOCAL_TIMELINE to getString().sign_in_instance_detail_tab_local_timeline,
            )
        }
    }

    val tab get() = tabs.getOrNull(currentIndex)?.first
    val currentIndex get() = currentTabIndex.value

    fun cacheScrollPosition(next: InstancesTab) {
        scrollPositions.value = scrollPositions.value.mapIndexed { index, state ->
            if (lazyListState.firstVisibleItemIndex > 0 && index == currentIndex) {
                lazyListState.firstVisibleItemIndex to lazyListState.firstVisibleItemScrollOffset
            } else {
                state
            }
        }

        currentTabIndex.value = tabs.indexOfFirst { it.first == next }
    }

    suspend fun restoreScrollPosition(tab: InstancesTab) {
        if (lazyListState.firstVisibleItemIndex == 0) {
            return
        }

        val (index, offset) = scrollPositions.value[tabs.indexOfFirst { it.first == tab }]

        lazyListState.scrollToItem(index, offset)
    }
}
