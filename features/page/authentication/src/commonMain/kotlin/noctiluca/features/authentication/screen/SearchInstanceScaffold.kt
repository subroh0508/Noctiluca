package noctiluca.features.authentication.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import noctiluca.features.authentication.SignInScreen
import noctiluca.features.authentication.component.QueryTextField
import noctiluca.features.authentication.getString
import noctiluca.features.authentication.section.InstanceSuggestsList
import noctiluca.features.authentication.viewmodel.MastodonInstanceListViewModel
import noctiluca.features.shared.atoms.appbar.CenterAlignedTopAppBar
import noctiluca.features.shared.model.LoadState
import noctiluca.features.shared.molecules.HeadlineWithProgress

private val HorizontalPadding = 16.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SearchInstanceScaffold(
    viewModel: MastodonInstanceListViewModel,
) {
    val navigator = LocalNavigator.current
    val uiModel by viewModel.uiModel.collectAsState()

    Scaffold(
        topBar = { CenterAlignedTopAppBar(getString().sign_in_page_title) },
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
        ) {
            Headline(uiModel.state.loading)

            QueryTextField(
                paddingValues,
                onDebouncedTextChange = { viewModel.search(it) },
                modifier = Modifier.padding(horizontal = HorizontalPadding),
            )

            InstanceSuggestsList(
                uiModel.suggests,
                uiModel.state is LoadState.Loaded<*>,
                onSelect = {
                    navigator?.push(SignInScreen(it.domain))
                },
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}

@Composable
private fun Headline(loading: Boolean) = HeadlineWithProgress(
    getString().sign_in_search_instance,
    loading,
    Modifier.padding(
        top = 12.dp,
        start = HorizontalPadding,
        end = HorizontalPadding,
    ),
)
