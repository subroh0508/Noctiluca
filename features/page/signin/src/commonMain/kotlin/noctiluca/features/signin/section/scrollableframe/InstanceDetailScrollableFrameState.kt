package noctiluca.features.signin.section.scrollableframe

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import noctiluca.features.signin.component.InstancesTab

internal class InstanceDetailScrollableFrameState private constructor(
    private val tabs: List<Int>,
    private var index: Int,
    private var scrollPositions: List<Pair<Int, Int>>,
) {
    constructor(
        tabs: List<InstancesTab>,
        scrollPositions: List<Pair<Int, Int>> = List(tabs.size) { 0 to 0 },
    ) : this(tabs = tabs.map { it.ordinal }, index = 0, scrollPositions = scrollPositions)

    companion object {
        const val TAB_INDEX = 1

        private const val TAB_ORDINAL_LIST = "tabOrdinalList"
        private const val SCROLL_POSITIONS = "scrollPositions"

        val Saver = mapSaver(
            save = {
                it.cacheScrollPosition()
                mapOf(
                    TAB_ORDINAL_LIST to it.tabs,
                    SCROLL_POSITIONS to listOf(it.currentScrollPositions) + it.scrollPositions,
                )
            },
            restore = { map ->
                @Suppress("UNCHECKED_CAST")
                val tabs = (map.getValue(TAB_ORDINAL_LIST) as? List<Int>) ?: listOf()

                @Suppress("UNCHECKED_CAST")
                val list = (map.getValue(SCROLL_POSITIONS) as? List<Pair<Int, Int>>) ?: listOf()

                val current = list.first()
                val scrollPositions = list.drop(1)

                InstanceDetailScrollableFrameState(
                    tabs,
                    scrollPositions.indexOf(current).takeIf { it >= 0 } ?: 0,
                    scrollPositions,
                )
            },
        )
    }

    val lazyListState by lazy {
        val (index, offset) = currentScrollPositions

        LazyListState(index, offset)
    }

    val firstVisibleItemIndex get() = lazyListState.firstVisibleItemIndex

    private val firstVisibleItemScrollOffset get() = lazyListState.firstVisibleItemScrollOffset
    private val currentScrollPositions get() = scrollPositions[index]

    fun cacheScrollPosition(prev: InstancesTab, next: InstancesTab) {
        cacheScrollPosition(tabs.indexOf(prev.ordinal))
        index = tabs.indexOf(next.ordinal)
    }

    suspend fun restoreScrollPosition(tab: InstancesTab) {
        if (firstVisibleItemIndex == 0) {
            return
        }

        val (index, offset) = scrollPositions[tabs.indexOf(tab.ordinal)]
        if (firstVisibleItemIndex >= TAB_INDEX && index == 0) {
            lazyListState.scrollToItem(TAB_INDEX, 0)
            return
        }

        lazyListState.scrollToItem(index, offset)
    }

    private fun cacheScrollPosition(cachedAt: Int = this.index) {
        scrollPositions = scrollPositions.mapIndexed { index, state ->
            if (firstVisibleItemIndex > 0 && index == cachedAt) {
                firstVisibleItemIndex to firstVisibleItemScrollOffset
            } else {
                state
            }
        }
    }
}

@Composable
internal fun rememberInstanceDetailScrollableFrameState(
    tab: InstancesTab,
    tabList: List<InstancesTab>,
): InstanceDetailScrollableFrameState {
    val scrollState = rememberSaveable(tabList, saver = InstanceDetailScrollableFrameState.Saver) {
        InstanceDetailScrollableFrameState(tabList)
    }

    LaunchedEffect(tab) {
        scrollState.restoreScrollPosition(tab)
    }
    return scrollState
}
