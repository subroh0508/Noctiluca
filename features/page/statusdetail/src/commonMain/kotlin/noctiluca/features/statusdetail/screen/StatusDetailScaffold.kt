package noctiluca.features.statusdetail.screen

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
import noctiluca.features.shared.extensions.getAuthorizedScreenModel
import noctiluca.features.statusdetail.StatusDetailScreen
import noctiluca.features.statusdetail.section.StatusContext
import noctiluca.features.statusdetail.section.StatusDetailTopAppBar
import noctiluca.features.statusdetail.viewmodel.StatusDetailViewModel
import noctiluca.model.StatusId
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun StatusDetailScreen.StatusDetailScaffold() {
    val viewModel: StatusDetailViewModel = getAuthorizedScreenModel {
        parametersOf(StatusId(id))
    }

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
        if (uiModel.loading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
                    .offset(y = paddingValues.calculateTopPadding()),
            )
            return@Scaffold
        }

        StatusContext(
            viewModel.id,
            uiModel.statuses,
            lazyListState,
            paddingValues,
            onClickStatus = { navigator?.push(it) },
            onClickAvatar = { navigator?.push(it) },
        )
    }
}
