package noctiluca.features.accountdetail.organisms.tab

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import noctiluca.features.accountdetail.getString
import noctiluca.features.accountdetail.viewmodel.AccountDetailViewModel
import noctiluca.features.shared.atoms.tab.PrimaryTabs

@Composable
internal fun AccountStatusesTabs(
    currentTab: AccountDetailViewModel.UiModel.Tab,
    statusesScrollState: AccountStatusesScrollState,
    onSwitch: (AccountDetailViewModel.UiModel.Tab) -> Unit = {},
    modifier: Modifier = Modifier,
) = PrimaryTabs(
    statusesScrollState.tabs,
    currentTab.ordinal,
    onClick = { _, (tab, _) ->
        statusesScrollState.cacheScrollPosition(currentTab)
        onSwitch(tab)
    },
    transform = { (_, label) -> label },
    modifier = modifier,
)

@Composable
internal fun rememberTabbedAccountStatusesState(
    tab: AccountDetailViewModel.UiModel.Tab,
): AccountStatusesScrollState {
    val scrollState = AccountStatusesScrollState()

    LaunchedEffect(tab) { scrollState.restoreScrollPosition(tab) }
    return scrollState
}

internal class AccountStatusesScrollState private constructor(
    val tabs: List<Pair<AccountDetailViewModel.UiModel.Tab, String>>,
    val lazyListState: LazyListState,
    private val scrollPositions: MutableState<List<Pair<Int, Int>>>,
) {
    companion object {
        @Composable
        operator fun invoke(
            lazyListState: LazyListState = rememberLazyListState(),
        ): AccountStatusesScrollState {
            val tabTitles = listOf(
                AccountDetailViewModel.UiModel.Tab.STATUSES to getString().account_detail_tab_statuses,
                AccountDetailViewModel.UiModel.Tab.STATUSES_AND_REPLIES to getString().account_detail_tab_statuses_and_replies,
                AccountDetailViewModel.UiModel.Tab.MEDIA to getString().account_detail_tab_media,
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

    fun cacheScrollPosition(prev: AccountDetailViewModel.UiModel.Tab) {
        scrollPositions.value = scrollPositions.value.mapIndexed { index, state ->
            if (lazyListState.firstVisibleItemIndex > 0 && index == prev.ordinal) {
                lazyListState.firstVisibleItemIndex to lazyListState.firstVisibleItemScrollOffset
            } else {
                state
            }
        }
    }

    suspend fun restoreScrollPosition(tab: AccountDetailViewModel.UiModel.Tab) {
        if (lazyListState.firstVisibleItemIndex == 0) {
            return
        }

        val (index, offset) = scrollPositions.value[tab.ordinal]

        lazyListState.scrollToItem(index, offset)
    }
}
