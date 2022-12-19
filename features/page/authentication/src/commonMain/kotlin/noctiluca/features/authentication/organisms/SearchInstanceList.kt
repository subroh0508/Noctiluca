package noctiluca.features.authentication.organisms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import noctiluca.components.atoms.DebouncedTextForm
import noctiluca.features.authentication.state.rememberMastodonInstances

private const val DEBOUNCE_TIME_MILLIS = 500L

@Composable
fun SearchInstanceList() {
    var query by remember { mutableStateOf("") }

    Column {
        InstanceDomain(query, onChangeQuery = { query = it })
        InstanceList(query)
    }
}

@Composable
private fun InstanceDomain(
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
private fun InstanceList(query: String) {
    if (query.isBlank()) {
        return
    }

    val instances by rememberMastodonInstances(query)

    LazyColumn {
        items(instances) {
            Column {
                Text(it.domain)
                Text(it.description ?: "")
            }
        }
    }
}
