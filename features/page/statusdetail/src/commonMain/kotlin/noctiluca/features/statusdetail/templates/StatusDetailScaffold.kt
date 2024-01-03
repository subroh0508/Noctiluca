package noctiluca.features.statusdetail.templates

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
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
import noctiluca.features.statusdetail.LocalResources
import noctiluca.features.statusdetail.component.StatusDetail
import noctiluca.features.statusdetail.component.StatusDetailTopAppBar
import noctiluca.features.statusdetail.viewmodel.StatusDetailViewModel
import noctiluca.model.StatusId
import noctiluca.model.status.StatusList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun StatusDetailScaffold(
    viewModel: StatusDetailViewModel,
) {
    val navigator = LocalNavigator.current
    val res = LocalResources.current

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
) = LazyColumn(
    state = lazyListState,
    contentPadding = PaddingValues(
        start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
        top = paddingValues.calculateTopPadding(),
        end = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
        bottom = 64.dp,
    ),
    modifier = Modifier.fillMaxSize(),
) {
    items(
        statuses.size,
        key = { i -> statuses[i].id.value },
    ) { i ->
        if (statuses[i].id == primary) {
            StatusDetail(statuses[i])
        }
    }
}
