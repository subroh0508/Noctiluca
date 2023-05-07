package noctiluca.features.components.molecules.scaffold

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabbedScaffold(
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    topAppBar: @Composable (TopAppBarScrollBehavior) -> Unit = {},
    bottomBar: @Composable BoxScope.() -> Unit = {},
    tabs: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) = Scaffold(
    topBar = { topAppBar(scrollBehavior) },
    snackbarHost = { SnackbarHost(snackbarHostState) },
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
) { paddingValues ->
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(paddingValues),
    ) {
        tabs()

        Box(Modifier.weight(1f)) {
            content(paddingValues)
            bottomBar()
        }
    }

    Box(Modifier.offset(y = paddingValues.calculateTopPadding())) {
        tabs()
    }
}
