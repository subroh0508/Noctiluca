package noctiluca.features.shared.atoms.textfield

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp

@Composable
fun TextArea(
    value: String?,
    onValueChange: (String?) -> Unit,
    supportingText: String? = null,
    modifier: Modifier = Modifier,
    trailingIcon: (@Composable () -> Unit)? = null,
) = BasicTextField(
    value ?: "",
    onValueChange = { onValueChange(it.takeIf(String::isNotBlank)) },
    modifier = modifier,
    textStyle = LocalTextStyle.current.copy(
        color = MaterialTheme.colorScheme.onSurface,
    ),
    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
    decorationBox = { innerTextField ->
        TextAreaDecorationBox(
            value,
            supportingText,
            innerTextField,
            trailingIcon,
        )
    }
)

@Composable
private fun TextAreaDecorationBox(
    text: String?,
    supportingText: String?,
    innerTextField: @Composable () -> Unit,
    trailingIcon: (@Composable () -> Unit)? = null,
) = Box {
    if (text.isNullOrBlank() && !supportingText.isNullOrBlank()) {
        Text(
            supportingText,
            style = LocalTextStyle.current.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
        )
    }

    Row(Modifier.fillMaxWidth()) {
        Box(Modifier.weight(1F)) {
            innerTextField()
        }
        trailingIcon?.let {
            Spacer(Modifier.width(16.dp))
            it()
        }
    }
}
