package noctiluca.features.signin.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import noctiluca.features.shared.components.textfield.DebouncedTextForm
import noctiluca.features.signin.getString

private const val DEBOUNCE_TIME_MILLIS = 500L

@Composable
internal fun QueryTextField(
    initQuery: String,
    onDebouncedTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) = Column(
    modifier = Modifier.padding()
) {
    var query by remember(initQuery) { mutableStateOf(initQuery) }

    DebouncedTextForm(
        query,
        DEBOUNCE_TIME_MILLIS,
        onDebouncedChange = onDebouncedTextChange,
    ) { textState ->
        Box(
            modifier = modifier.padding(bottom = 8.dp),
        ) {
            OutlinedTextField(
                textState.value,
                { textState.value = it },
                placeholder = { Text(getString().sign_in_search_instance_hint) },
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
            )

            ClearQueryIcon(
                textState.value,
                onClick = { query = "" },
                modifier = Modifier.align(Alignment.CenterEnd),
            )
        }
    }
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
