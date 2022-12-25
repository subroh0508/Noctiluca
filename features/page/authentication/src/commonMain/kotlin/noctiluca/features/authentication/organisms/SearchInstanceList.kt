package noctiluca.features.authentication.organisms

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import noctiluca.components.atoms.image.AsyncImage
import noctiluca.components.atoms.image.imageResources
import noctiluca.components.atoms.list.LeadingAvatarContainerSize
import noctiluca.components.atoms.list.OneLineListItem
import noctiluca.components.atoms.list.ThreeLineListItem
import noctiluca.components.atoms.textfield.DebouncedTextForm
import noctiluca.components.atoms.textfield.SingleLineTextField
import noctiluca.components.model.LoadState
import noctiluca.components.molecules.HeadlineWithProgress
import noctiluca.features.authentication.getDrawables
import noctiluca.features.authentication.getString
import noctiluca.features.authentication.state.rememberMastodonInstanceSuggests
import noctiluca.instance.model.Instance

private const val DEBOUNCE_TIME_MILLIS = 500L
private val horizontalPadding = 16.dp

@Composable
fun SearchInstanceList(
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var query by remember { mutableStateOf("") }
    val state by rememberMastodonInstanceSuggests(query)

    val instances: List<Instance.Suggest> = state.getValueOrNull() ?: listOf()

    SearchInstanceListLayout(
        state,
        instances,
        query,
        onChangeQuery = { query = it },
        onSelect = onSelect,
        modifier = modifier,
    )
}

@Composable
internal fun SearchInstanceListLayout(
    state: LoadState,
    instances: List<Instance.Suggest>,
    query: String,
    onChangeQuery: (String) -> Unit,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) = Column(modifier) {
    Headline(state.loading)

    InstanceDomain(query, onChangeQuery = onChangeQuery)
    InstanceList(instances, state.loaded, onSelect)
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
private fun InstanceDomain(
    query: String,
    onChangeQuery: (String) -> Unit,
) = DebouncedTextForm(
    query,
    DEBOUNCE_TIME_MILLIS,
    onDebouncedChange = onChangeQuery,
) { textState ->
    SingleLineTextField(
        textState.value,
        { textState.value = it },
        placeholder = { Text(getString().sign_in_search_instance_hint) },
        keyboardType = KeyboardType.Email,
        modifier = Modifier.fillMaxWidth()
            .padding(
                bottom = 8.dp,
                start = horizontalPadding,
                end = horizontalPadding,
            ),
    )
}

@Composable
private fun InstanceList(
    instances: List<Instance.Suggest>,
    loaded: Boolean,
    onSelect: (String) -> Unit,
) {
    if (loaded && instances.isEmpty()) {
        OneLineListItem(getString().sign_in_search_instances_empty)
        return
    }

    LazyColumn(
        state = rememberLazyListState(),
        modifier = Modifier.padding(horizontal = horizontalPadding),
    ) {
        items(instances) {
            ThreeLineListItem(
                it.domain,
                supportingText = it.description ?: "",
                leadingContent = {
                    AsyncImage(
                        it.thumbnail,
                        fallback = imageResources(getDrawables().icon_mastodon),
                        modifier = Modifier.size(LeadingAvatarContainerSize),
                    )
                },
                modifier = Modifier.selectable(
                    selected = false,
                    onClick = { onSelect(it.domain) },
                ),
            )
        }
    }
}
