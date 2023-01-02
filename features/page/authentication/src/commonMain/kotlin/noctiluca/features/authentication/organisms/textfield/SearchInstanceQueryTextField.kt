package noctiluca.features.authentication.organisms.textfield

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import noctiluca.components.atoms.list.OneLineListItem
import noctiluca.components.atoms.textfield.DebouncedTextForm
import noctiluca.components.atoms.textfield.SingleLineTextField
import noctiluca.components.model.LoadState
import noctiluca.features.authentication.getString
import noctiluca.features.authentication.model.QueryText
import noctiluca.features.authentication.state.rememberMastodonInstanceSuggests
import noctiluca.instance.model.Instance

private const val DEBOUNCE_TIME_MILLIS = 500L

@Composable
internal fun SearchInstanceQueryTextField(
    onLoading: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    listContent: @Composable (List<Instance.Suggest>, MutableState<QueryText>) -> Unit,
) {
    val query = remember { mutableStateOf<QueryText>(QueryText.Empty) }
    val instances by rememberMastodonInstanceSuggests(query.value)

    LaunchedEffect(instances.loading) { onLoading(instances.loading) }

    Box(modifier) {
        DebouncedTextForm(
            query.value.text,
            DEBOUNCE_TIME_MILLIS,
            onDebouncedChange = { handleOnDebouncedTextChanged(query, it) },
        ) { textState ->
            SingleLineTextField(
                textState.value,
                { textState.value = it },
                enabled = query.value !is QueryText.Static,
                placeholder = { Text(getString().sign_in_search_instance_hint) },
                keyboardType = KeyboardType.Email,
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = 8.dp),
            )
        }
    }

    SearchResultList(instances) { listContent(it, query) }
}

@Composable
private fun SearchResultList(
    instancesLoadState: LoadState,
    content: @Composable (List<Instance.Suggest>) -> Unit,
) {
    val instances: List<Instance.Suggest> = instancesLoadState.getValueOrNull() ?: listOf()

    if (instancesLoadState.loaded && instances.isEmpty()) {
        OneLineListItem(getString().sign_in_search_instances_empty)
        return
    }

    content(instances)
}

private fun handleOnDebouncedTextChanged(
    query: MutableState<QueryText>,
    text: String,
) {
    if (query.value is QueryText.Static) {
        return
    }

    query.value = QueryText(text)
}
