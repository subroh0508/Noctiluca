package noctiluca.features.authentication.templates

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import noctiluca.components.atoms.textfield.DebouncedTextForm
import noctiluca.components.atoms.textfield.SingleLineTextField
import noctiluca.components.molecules.HeadlineWithProgress
import noctiluca.features.authentication.getString
import noctiluca.features.authentication.organisms.SearchInstanceList

private const val DEBOUNCE_TIME_MILLIS = 500L
private val horizontalPadding = 16.dp

@Composable
fun SelectInstanceForm(
    modifier: Modifier = Modifier,
) {
    var query by remember { mutableStateOf("") }
    var domain by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    Column(modifier) {
        Headline(loading)

        InstanceDomainTextField(
            domain.ifBlank { query },
            domain.isBlank(),
            onChangeQuery = { query = it },
        )
        SearchInstanceList(
            query,
            domain,
            onChangeLoading = { loading = it },
            onSelect = { domain = it },
            modifier = Modifier.padding(horizontal = horizontalPadding),
        )
    }
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
private fun InstanceDomainTextField(
    value: String,
    enabled: Boolean,
    onChangeQuery: (String) -> Unit,
) = DebouncedTextForm(
    value,
    DEBOUNCE_TIME_MILLIS,
    onDebouncedChange = onChangeQuery,
) { textState ->
    SingleLineTextField(
        textState.value,
        { textState.value = it },
        enabled = enabled,
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
