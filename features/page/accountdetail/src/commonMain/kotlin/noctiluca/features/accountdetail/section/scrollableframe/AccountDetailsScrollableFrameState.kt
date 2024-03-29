package noctiluca.features.accountdetail.section.scrollableframe

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import noctiluca.features.accountdetail.component.AccountDetailTabList
import noctiluca.model.accountdetail.StatusesQuery

internal class AccountDetailScrollableFrameState private constructor(
    private var index: Int,
    private var scrollPositions: List<Pair<Int, Int>>,
) {
    constructor(
        queries: List<StatusesQuery>,
        scrollPositions: List<Pair<Int, Int>> = List(queries.size) { 0 to 0 },
    ) : this(index = 0, scrollPositions = scrollPositions)

    companion object {
        const val TAB_INDEX = 1

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

    suspend fun restoreScrollPosition(query: StatusesQuery) {
        if (firstVisibleItemIndex == 0) {
            return
        }

        val (index, offset) = scrollPositions[query.ordinal]
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
internal fun rememberAccountDetailScrollableFrameState(
    current: StatusesQuery,
): AccountDetailScrollableFrameState {
    val scrollState = rememberSaveable(saver = AccountDetailScrollableFrameState.Saver) {
        AccountDetailScrollableFrameState(AccountDetailTabList)
    }

    LaunchedEffect(current) { scrollState.restoreScrollPosition(current) }
    return scrollState
}
