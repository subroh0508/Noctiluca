package noctiluca.features.authentication.organisms.tab

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import noctiluca.features.authentication.getString
import noctiluca.features.shared.atoms.tab.PrimaryTabs
import noctiluca.model.authentication.Instance

enum class InstancesTab {
    INFO, EXTENDED_DESCRIPTION, LOCAL_TIMELINE
}

@Composable
internal fun InstanceDetailTabs(
    statusesScrollState: InstanceDetailScrollState,
    modifier: Modifier = Modifier,
) {
    if (statusesScrollState.tabs.isEmpty()) {
        return
    }

    PrimaryTabs(
        statusesScrollState.tabs,
        statusesScrollState.currentIndex,
        onClick = { _, (tab, _) -> statusesScrollState.cacheScrollPosition(tab) },
        transform = { (_, label) -> label },
        modifier = modifier,
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

    val tab get() = tabs[currentTabIndex.value].first
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
