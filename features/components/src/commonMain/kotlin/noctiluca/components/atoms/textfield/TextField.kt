package noctiluca.components.atoms.textfield

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun SingleLineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholder: @Composable (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
) = OutlinedTextField(
    value,
    onValueChange,
    modifier,
    enabled = enabled,
    placeholder = placeholder,
    singleLine = true,
    maxLines = 1,
    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
)
