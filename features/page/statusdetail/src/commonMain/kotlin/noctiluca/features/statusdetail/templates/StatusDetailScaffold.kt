package noctiluca.features.statusdetail.templates

import androidx.compose.foundation.layout.*
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
import noctiluca.features.shared.molecules.list.LazyColumn
import noctiluca.features.shared.status.Status
import noctiluca.features.statusdetail.LocalResources
import noctiluca.features.statusdetail.component.StatusDetail
import noctiluca.features.statusdetail.component.StatusDetailTopAppBar
import noctiluca.features.statusdetail.viewmodel.StatusDetailViewModel
import noctiluca.model.StatusId
import noctiluca.model.status.StatusList
import androidx.compose.material3.Divider as MaterialDivider

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
) {
    MaterialDivider(
        modifier = Modifier.fillMaxWidth()
            .offset(y = paddingValues.calculateTopPadding()),
    )

    LazyColumn(
        statuses,
        key = { it.id.value },
        state = lazyListState,
        showDivider = true,
        contentPadding = PaddingValues(
            start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
            top = paddingValues.calculateTopPadding(),
            end = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
            bottom = 64.dp,
        ),
        modifier = Modifier.fillMaxSize(),
    ) { _, status ->
        if (status.id == primary) {
            StatusDetail(
                status,
                onClickAction = { },
            )
        } else {
            Status(
                status,
                onClick = { },
                onClickAction = { },
            )
        }
    }
}
