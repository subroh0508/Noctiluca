package noctiluca.features.authentication.organisms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import noctiluca.components.atoms.image.AsyncImage
import noctiluca.components.atoms.image.imageResources
import noctiluca.components.atoms.list.ListItem
import noctiluca.components.atoms.textfield.DebouncedTextForm
import noctiluca.components.utils.ImageLoader
import noctiluca.features.authentication.getDrawables
import noctiluca.features.authentication.state.rememberMastodonInstances
import noctiluca.instance.model.Instance
import noctiluca.model.Uri

private const val DEBOUNCE_TIME_MILLIS = 500L
private const val PATH_FALLBACK_ICON = "${ImageLoader.RESOURCES_DIRECTORY}/icon_mastodon.png"

@Composable
fun SearchInstanceList() {
    var query by remember { mutableStateOf("") }
    val instances by rememberMastodonInstances(query)

    Column {
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
        onValueChange = { textState.value = it },
    )
}

@Composable
internal fun InstanceList(
    instances: List<Instance>,
) = LazyColumn {
    items(instances) {
        ListItem(
            it.domain,
            supportingText = it.description,
            leadingContent = {
                AsyncImage(
                    null, //it.thumbnail,
                    fallback = imageResources(getDrawables().icon_mastodon),
                )
            },
        )
    }
}
