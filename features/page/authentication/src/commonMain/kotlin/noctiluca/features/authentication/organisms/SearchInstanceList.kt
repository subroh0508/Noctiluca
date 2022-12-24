package noctiluca.features.authentication.organisms

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
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
import noctiluca.components.atoms.textfield.DebouncedTextForm
import noctiluca.features.authentication.getDrawables
import noctiluca.features.authentication.state.rememberMastodonInstances
import noctiluca.instance.model.Instance

private const val DEBOUNCE_TIME_MILLIS = 500L

@Composable
fun SearchInstanceList() {
    var query by remember { mutableStateOf("") }
    val instances by rememberMastodonInstances(query)

    Column(modifier = Modifier.padding(16.dp)) {
        InstanceDomain(query, onChangeQuery = { query = it })
        InstanceList(instances)
    }
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
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email,
        ),
        onValueChange = { textState.value = it },
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
internal fun InstanceList(
    instances: List<Instance>,
) = LazyColumn(
    state = rememberLazyListState(),
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
        Divider(thickness = Dp(1 / LocalDensity.current.density))
    }
}
