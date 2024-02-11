package noctiluca.features.statusdetail.templates

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import noctiluca.features.navigation.navigateToAccountDetail
import noctiluca.features.navigation.navigateToStatusDetail
import noctiluca.features.statusdetail.component.Position
import noctiluca.features.statusdetail.component.StatusDetail
import noctiluca.features.statusdetail.component.StatusDetailTopAppBar
import noctiluca.features.statusdetail.component.StatusItem
import noctiluca.features.statusdetail.viewmodel.StatusDetailViewModel
import noctiluca.model.AccountId
import noctiluca.model.StatusId
import noctiluca.model.status.StatusList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun StatusDetailScaffold(
    viewModel: StatusDetailViewModel,
) {
    val navigator = LocalNavigator.current

    val scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val lazyListState = rememberLazyListState()
    val snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }

    val uiModel by viewModel.uiModel.collectAsState()

    Scaffold(
        topBar = {
            StatusDetailTopAppBar()
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValues ->
        when (uiModel) {
            is StatusDetailViewModel.UiModel.Loading -> LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
                    .offset(y = paddingValues.calculateTopPadding()),
            )

            is StatusDetailViewModel.UiModel.Loaded -> StatusContext(
                viewModel.id,
                uiModel.getValue(),
                lazyListState,
                paddingValues,
                onClickStatus = { navigator?.navigateToStatusDetail(it) },
                onClickAvatar = { navigator?.navigateToAccountDetail(it) },
            )
        }
    }
}

@Composable
private fun StatusContext(
    primary: StatusId,
    statuses: StatusList,
    lazyListState: LazyListState,
    paddingValues: PaddingValues,
    onClickStatus: (StatusId) -> Unit,
    onClickAvatar: (AccountId) -> Unit,
) {
    HorizontalDivider(
        modifier = Modifier.offset(y = paddingValues.calculateTopPadding()),
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = lazyListState,
        contentPadding = PaddingValues(
            start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
            top = paddingValues.calculateTopPadding(),
            end = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
            bottom = 64.dp,
        ),
    ) {
        itemsIndexed(
            statuses,
            key = { _, status -> status.id.value },
        ) { index, status ->
            if (status.id == primary) {
                StatusDetail(
                    status,
                    onClickAvatar = onClickAvatar,
                    onClickAction = { },
                )
                return@itemsIndexed
            }

            val position = when (index) {
                0 -> Position.TOP
                statuses.size - 1 -> Position.BOTTOM
                else -> Position.MIDDLE
            }

            StatusItem(
                status,
                position,
                onClickStatus = onClickStatus,
                onClickAvatar = onClickAvatar,
                onClickAction = { },
            )
        }
    }
}
