package noctiluca.features.accountdetail.organisms.tab

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import noctiluca.features.accountdetail.getString
import noctiluca.features.accountdetail.state.AccountStatuses
import noctiluca.features.accountdetail.state.AccountStatusesState
import noctiluca.features.components.atoms.tab.PrimaryTabs

@Composable
internal fun AccountStatusesTabs(
    state: AccountStatusesState,
    statusesScrollState: AccountStatusesScrollState,
    modifier: Modifier = Modifier,
) {
    val (currentTab) = state.value

    PrimaryTabs(
        statusesScrollState.tabs,
        currentTab.ordinal,
        onClick = { _, (tab, _) ->
            statusesScrollState.cacheScrollPosition(currentTab)
            state.switch(tab)
        },
        transform = { (_, label) -> label },
        modifier = modifier,
    )
}

@Composable
internal fun rememberTabbedAccountStatusesState(
    tab: AccountStatuses.Tab,
): AccountStatusesScrollState {
    val scrollState = AccountStatusesScrollState()

    LaunchedEffect(tab) { scrollState.restoreScrollPosition(tab) }
    return scrollState
}

internal class AccountStatusesScrollState private constructor(
    val tabs: List<Pair<AccountStatuses.Tab, String>>,
    val lazyListState: LazyListState,
    private val scrollPositions: MutableState<List<Pair<Int, Int>>>,
) {
    companion object {
        @Composable
        operator fun invoke(
            lazyListState: LazyListState = rememberLazyListState(),
        ): AccountStatusesScrollState {
            val tabTitles = listOf(
                AccountStatuses.Tab.STATUSES to getString().account_detail_tab_statuses,
                AccountStatuses.Tab.STATUSES_AND_REPLIES to getString().account_detail_tab_statuses_and_replies,
                AccountStatuses.Tab.MEDIA to getString().account_detail_tab_media,
            )

            val scrollPositions = remember {
                mutableStateOf(List(tabTitles.size) { 1 to 0 })
            }

            return remember {
                AccountStatusesScrollState(
                    tabTitles,
                    lazyListState,
                    scrollPositions,
                )
            }
        }
    }

    fun cacheScrollPosition(prev: AccountStatuses.Tab) {
        scrollPositions.value = scrollPositions.value.mapIndexed { index, state ->
            if (lazyListState.firstVisibleItemIndex > 0 && index == prev.ordinal) {
                lazyListState.firstVisibleItemIndex to lazyListState.firstVisibleItemScrollOffset
            } else {
                state
            }
        }
    }

    suspend fun restoreScrollPosition(tab: AccountStatuses.Tab) {
        if (lazyListState.firstVisibleItemIndex == 0) {
            return
        }

        val (index, offset) = scrollPositions.value[tab.ordinal]

        lazyListState.scrollToItem(index, offset)
    }
}
