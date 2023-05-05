package noctiluca.features.authentication.organisms.tab

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noctiluca.features.authentication.getString
import noctiluca.features.authentication.state.Instances
import noctiluca.instance.model.Instance

@Composable
internal fun InstanceDetailTabs(
    statusesScrollState: InstanceDetailScrollState,
    modifier: Modifier = Modifier,
) {
    if (statusesScrollState.tabs.isEmpty()) {
        return
    }

    TabRow(
        selectedTabIndex = statusesScrollState.currentIndex,
        modifier = modifier,
    ) {
        statusesScrollState.tabs.forEach { (tab, label) ->
            Tab(
                selected = tab == statusesScrollState.tab,
                onClick = { statusesScrollState.cacheScrollPosition(tab) },
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.height(48.dp),
                ) { Text(label) }
            }
        }
    }
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
    val tabs: List<Pair<Instances.Tab, String>>,
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
        ): List<Pair<Instances.Tab, String>> {
            instance ?: return listOf()

            @Suppress("MagicNumber")
            return listOfNotNull(
                Instances.Tab.INFO to getString().sign_in_instance_detail_tab_info,
                if ((instance.version?.major ?: 0) >= 4) {
                    Instances.Tab.EXTENDED_DESCRIPTION to getString().sign_in_instance_detail_tab_extended_description
                } else {
                    null
                },
                Instances.Tab.LOCAL_TIMELINE to getString().sign_in_instance_detail_tab_local_timeline,
            )
        }
    }

    val tab get() = tabs[currentTabIndex.value].first
    val currentIndex get() = currentTabIndex.value

    fun cacheScrollPosition(next: Instances.Tab) {
        scrollPositions.value = scrollPositions.value.mapIndexed { index, state ->
            if (lazyListState.firstVisibleItemIndex > 0 && index == currentIndex) {
                lazyListState.firstVisibleItemIndex to lazyListState.firstVisibleItemScrollOffset
            } else {
                state
            }
        }

        currentTabIndex.value = tabs.indexOfFirst { it.first == next }
    }

    suspend fun restoreScrollPosition(tab: Instances.Tab) {
        if (lazyListState.firstVisibleItemIndex == 0) {
            return
        }

        val (index, offset) = scrollPositions.value[tabs.indexOfFirst { it.first == tab }]

        lazyListState.scrollToItem(index, offset)
    }
}
