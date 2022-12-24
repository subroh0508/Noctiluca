package noctiluca.features.authentication.organisms

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import noctiluca.components.atoms.image.AsyncImage
import noctiluca.components.atoms.image.imageResources
import noctiluca.components.atoms.list.LeadingAvatarContainerSize
import noctiluca.components.atoms.list.ThreeLineListItem
import noctiluca.components.atoms.text.HeadlineSmall
import noctiluca.components.atoms.textfield.DebouncedTextForm
import noctiluca.features.authentication.getDrawables
import noctiluca.features.authentication.getString
import noctiluca.features.authentication.state.rememberMastodonInstances
import noctiluca.instance.model.Instance

private const val DEBOUNCE_TIME_MILLIS = 500L
private val horizontalPadding = 16.dp

@Composable
fun SearchInstanceList(modifier: Modifier = Modifier) {
    var query by remember { mutableStateOf("") }
    val state by rememberMastodonInstances(query)

    val instances: List<Instance> = state.getValueOrNull() ?: listOf()

    Column(modifier) {
        Headline(state.loading)

        InstanceDomain(query, onChangeQuery = { query = it })
        InstanceList(instances)
    }
}

@Composable
internal fun Headline(loading: Boolean) {
    if (loading) {
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    } else {
        Spacer(Modifier.height(4.dp))
    }

    HeadlineSmall(
        getString().sign_in_search_instance,
        Modifier.padding(
            top = 12.dp,
            start = horizontalPadding,
            end = horizontalPadding,
        ),
    )
    Spacer(Modifier.height(32.dp))
}

@Composable
internal fun InstanceDomain(
    query: String,
    onChangeQuery: (String) -> Unit,
) = DebouncedTextForm(
    query,
    DEBOUNCE_TIME_MILLIS,
    onDebouncedChange = onChangeQuery,
) { textState ->
    OutlinedTextField(
        textState.value,
        placeholder = { Text(getString().sign_in_search_instance_hint) },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email,
        ),
        onValueChange = { textState.value = it },
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = horizontalPadding),
    )
}

@Composable
internal fun InstanceList(
    instances: List<Instance>,
) = LazyColumn(
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
        )
        //Divider(thickness = Dp(1 / LocalDensity.current.density))
    }
}
