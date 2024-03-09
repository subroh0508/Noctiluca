package noctiluca.features.statusdetail.templates

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import cafe.adriel.voyager.navigator.LocalNavigator
import noctiluca.features.statusdetail.section.StatusContext
import noctiluca.features.statusdetail.section.StatusDetailTopAppBar
import noctiluca.features.statusdetail.viewmodel.StatusDetailViewModel

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
                onClickStatus = { navigator?.push(it) },
                onClickAvatar = { navigator?.push(it) },
            )
        }
    }
}
