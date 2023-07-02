package noctiluca.features.authentication.organisms.textfield

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import noctiluca.features.authentication.getString
import noctiluca.features.authentication.viewmodel.MastodonInstanceListViewModel
import noctiluca.features.components.atoms.list.OneLineListItem
import noctiluca.features.components.atoms.textfield.DebouncedTextForm
import noctiluca.features.components.atoms.textfield.SingleLineTextField
import noctiluca.features.components.model.LoadState
import noctiluca.instance.model.Instance

private const val DEBOUNCE_TIME_MILLIS = 500L

@Composable
internal fun SearchInstanceQueryTextField(
    uiModel: MastodonInstanceListViewModel.UiModel,
    paddingValues: PaddingValues,
    onDebouncedTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    headline: @Composable (Boolean) -> Unit = {},
    listContent: @Composable (List<Instance.Suggest>) -> Unit,
) = Column(
    modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
) {
    var query by remember(uiModel.query) { mutableStateOf(uiModel.query) }

    headline(uiModel.suggests.loading)

    DebouncedTextForm(
        query,
        DEBOUNCE_TIME_MILLIS,
        onDebouncedChange = onDebouncedTextChange,
    ) { textState ->
        Box(
            modifier = modifier.padding(bottom = 8.dp),
        ) {
            SingleLineTextField(
                textState.value,
                { textState.value = it },
                placeholder = { Text(getString().sign_in_search_instance_hint) },
                keyboardType = KeyboardType.Email,
                modifier = Modifier.fillMaxWidth(),
            )

            ClearQueryIcon(
                textState.value,
                onClick = { query = "" },
                modifier = Modifier.align(Alignment.CenterEnd),
            )
        }
    }

    SearchResultList(uiModel.suggests) { listContent(it) }
}

@Composable
private fun ClearQueryIcon(
    query: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (query.isBlank()) {
        return
    }

    IconButton(
        onClick,
        modifier = modifier,
    ) {
        Icon(
            Icons.Default.Clear,
            contentDescription = "clear",
        )
    }
}

@Composable
private fun SearchResultList(
    suggestsLoadState: LoadState,
    content: @Composable (List<Instance.Suggest>) -> Unit,
) {
    val suggests: List<Instance.Suggest> = suggestsLoadState.getValueOrNull() ?: listOf()

    if (suggestsLoadState.loaded && suggests.isEmpty()) {
        OneLineListItem(getString().sign_in_search_instances_empty)
        return
    }

    content(suggests)
}
