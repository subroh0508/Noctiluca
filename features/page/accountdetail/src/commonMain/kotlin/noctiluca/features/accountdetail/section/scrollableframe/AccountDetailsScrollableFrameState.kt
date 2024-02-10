package noctiluca.features.accountdetail.section.scrollableframe

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import noctiluca.model.accountdetail.StatusesQuery

internal class AccountDetailScrollableFrameState private constructor(
    private var index: Int,
    private var scrollPositions: List<Pair<Int, Int>>,
) {
    constructor(
        queries: List<StatusesQuery>,
        scrollPositions: List<Pair<Int, Int>> = List(queries.size) { 1 to 0 },
    ) : this(index = 0, scrollPositions = scrollPositions)

    companion object {
        val Saver = listSaver(
            save = {
                it.cacheScrollPosition()
                listOf(it.currentScrollPositions) + it.scrollPositions
            },
            restore = { list ->
                val current = list.first()
                val scrollPositions = list.drop(1)

                AccountDetailScrollableFrameState(
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

    fun cacheScrollPosition(prev: StatusesQuery, next: StatusesQuery) {
        cacheScrollPosition(prev.ordinal)
        index = next.ordinal
    }

    suspend fun restoreScrollPosition(tab: StatusesQuery) {
        if (firstVisibleItemIndex == 0) {
            return
        }

        val (index, offset) = scrollPositions[tab.ordinal]

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
internal fun rememberAccountDetailScrollableFrameState(
    current: StatusesQuery,
    queries: List<StatusesQuery>,
): AccountDetailScrollableFrameState {
    val scrollState = rememberSaveable(saver = AccountDetailScrollableFrameState.Saver) {
        AccountDetailScrollableFrameState(queries)
    }

    LaunchedEffect(current) { scrollState.restoreScrollPosition(current) }
    return scrollState
}
