package noctiluca.features.authentication.section.scrollableframe

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import noctiluca.features.authentication.component.InstancesTab
import noctiluca.model.authentication.Instance

internal class InstanceDetailScrollableFrameState private constructor(
    val tabs: List<Int>,
    private val index: MutableState<Int>,
    private var scrollPositions: List<Pair<Int, Int>>,
) {
    companion object {
        private const val VERSION_REQUIRE_EXTENDED_DESCRIPTION = 4

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
                    mutableStateOf(scrollPositions.indexOf(current).takeIf { it >= 0 } ?: 0),
                    scrollPositions,
                )
            },
        )

        operator fun invoke(
            instance: Instance?,
        ): InstanceDetailScrollableFrameState {
            val tabs = listOfNotNull(
                InstancesTab.INFO,
                if ((instance?.version?.major ?: 0) >= VERSION_REQUIRE_EXTENDED_DESCRIPTION) {
                    InstancesTab.EXTENDED_DESCRIPTION
                } else {
                    null
                },
                InstancesTab.LOCAL_TIMELINE,
            )

            return InstanceDetailScrollableFrameState(
                tabs = tabs.map { it.ordinal },
                index = mutableStateOf(0),
                scrollPositions = List(tabs.size) { 0 to 0 },
            )
        }
    }

    val lazyListState by lazy {
        val (index, offset) = currentScrollPositions

        LazyListState(index, offset)
    }

    val firstVisibleItemIndex get() = lazyListState.firstVisibleItemIndex

    private val firstVisibleItemScrollOffset get() = lazyListState.firstVisibleItemScrollOffset
    private val currentScrollPositions get() = scrollPositions[currentIndex]

    val tab get() = tabs.getOrNull(index.value)?.let { InstancesTab.entries[it] }
    val currentIndex get() = index.value

    fun cacheScrollPosition(next: InstancesTab) {
        cacheScrollPosition()
        index.value = tabs.indexOf(next.ordinal)
    }

    suspend fun restoreScrollPosition() {
        if (firstVisibleItemIndex == 0) {
            return
        }

        val (index, offset) = scrollPositions[currentIndex]

        lazyListState.scrollToItem(index, offset)
    }

    private fun cacheScrollPosition(cachedAt: Int = this.currentIndex) {
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
    instance: Instance?,
): InstanceDetailScrollableFrameState {
    val scrollState = rememberSaveable(saver = InstanceDetailScrollableFrameState.Saver) {
        InstanceDetailScrollableFrameState(instance)
    }

    LaunchedEffect(scrollState.currentIndex) {
        scrollState.restoreScrollPosition()
    }
    return scrollState
}
