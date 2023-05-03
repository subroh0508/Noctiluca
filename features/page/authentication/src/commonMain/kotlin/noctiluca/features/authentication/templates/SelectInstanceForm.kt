package noctiluca.features.authentication.templates

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import noctiluca.features.authentication.getString
import noctiluca.features.authentication.model.QueryText
import noctiluca.features.authentication.organisms.card.InstanceCard
import noctiluca.features.authentication.organisms.card.SuggestCard
import noctiluca.features.authentication.organisms.textfield.SearchInstanceQueryTextField
import noctiluca.features.components.molecules.HeadlineWithProgress
import noctiluca.features.components.molecules.list.LazyColumn
import noctiluca.instance.model.Instance

private val horizontalPadding = 16.dp

@Composable
internal fun SelectInstanceForm(
    modifier: Modifier = Modifier,
) = Column(modifier) {
    var loading by remember { mutableStateOf(false) }

    Headline(loading)

    SearchInstanceQueryTextField(
        onLoading = { loading = it },
        modifier = Modifier.padding(horizontal = horizontalPadding),
        listContent = { suggests, query ->
            when (query.value) {
                is QueryText.Static -> InstanceCard(
                    query.value,
                    onLoading = { loading = it },
                    modifier = Modifier.padding(horizontal = horizontalPadding),
                )
                else -> InstanceSuggestsList(
                    suggests,
                    onSelect = { query.value = QueryText(it) },
                )
            }
        },
    )
}

@Composable
private fun Headline(loading: Boolean) = HeadlineWithProgress(
    getString().sign_in_search_instance,
    loading,
    Modifier.padding(
        top = 12.dp,
        start = horizontalPadding,
        end = horizontalPadding,
    ),
)

@Composable
private fun InstanceSuggestsList(
    instances: List<Instance.Suggest>,
    onSelect: (Instance.Suggest) -> Unit,
    modifier: Modifier = Modifier,
) {
    // val focusManager = LocalFocusManager.current

    LazyColumn(
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
}
