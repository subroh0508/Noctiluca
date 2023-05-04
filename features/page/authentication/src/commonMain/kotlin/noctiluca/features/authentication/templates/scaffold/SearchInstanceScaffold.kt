package noctiluca.features.authentication.templates.scaffold

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noctiluca.features.authentication.getString
import noctiluca.features.authentication.organisms.card.SuggestCard
import noctiluca.features.authentication.organisms.textfield.SearchInstanceQueryTextField
import noctiluca.features.components.atoms.appbar.TopAppBar
import noctiluca.features.components.molecules.HeadlineWithProgress
import noctiluca.features.components.molecules.list.LazyColumn
import noctiluca.instance.model.Instance

private val HorizontalPadding = 16.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SearchInstanceScaffold(
    onSelect: (Instance.Suggest) -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) = Scaffold(
    topBar = { TopAppBar(getString().sign_in_page_title) },
    snackbarHost = { SnackbarHost(snackbarHostState) },
) { paddingValues ->
    SearchInstanceQueryTextField(
        paddingValues = paddingValues,
        modifier = Modifier.padding(horizontal = HorizontalPadding),
        headline = { loading -> Headline(loading) },
        listContent = { suggests ->
            InstanceSuggestsList(
                suggests,
                onSelect = onSelect,
            )
        },
    )
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

@Composable
private fun InstanceSuggestsList(
    instances: List<Instance.Suggest>,
    onSelect: (Instance.Suggest) -> Unit,
    modifier: Modifier = Modifier,
) = LazyColumn(
    instances,
    key = { it.domain },
    contentPadding = PaddingValues(
        horizontal = 16.dp,
        vertical = 8.dp,
    ),
    modifier = modifier,
) { _, suggest ->
    SuggestCard(
        suggest,
        onSelect,
        modifier = Modifier.padding(bottom = 16.dp),
    )
}
