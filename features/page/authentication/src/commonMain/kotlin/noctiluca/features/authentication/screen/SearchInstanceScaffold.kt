package noctiluca.features.authentication.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import noctiluca.features.authentication.SignInScreen
import noctiluca.features.authentication.component.QueryTextField
import noctiluca.features.authentication.getString
import noctiluca.features.authentication.section.InstanceSuggestsList
import noctiluca.features.authentication.viewmodel.MastodonInstanceListViewModel
import noctiluca.features.shared.model.LoadState

private val HorizontalPadding = 16.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SearchInstanceScaffold(
    viewModel: MastodonInstanceListViewModel,
    lazyListState: LazyListState,
) {
    val navigator = LocalNavigator.current
    val uiModel by viewModel.uiModel.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                { Text(getString().sign_in_page_title) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface,
                ),
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
        ) {
            Headline(uiModel.state.loading)

            QueryTextField(
                uiModel.query,
                onDebouncedTextChange = { viewModel.search(it) },
                modifier = Modifier.padding(horizontal = HorizontalPadding),
            )

            InstanceSuggestsList(
                uiModel.suggests,
                uiModel.state is LoadState.Loaded,
                lazyListState,
                onSelect = {
                    navigator?.push(SignInScreen(it.domain))
                },
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}

@Composable
private fun Headline(loading: Boolean) {
    if (loading) {
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    } else {
        Spacer(Modifier.height(4.dp))
    }

    Text(
        getString().sign_in_search_instance,
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(
            top = 12.dp,
            start = HorizontalPadding,
            end = HorizontalPadding,
        ),
    )
    Spacer(Modifier.height(32.dp))
}
