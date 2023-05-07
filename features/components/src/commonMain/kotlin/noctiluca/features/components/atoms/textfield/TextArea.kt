package noctiluca.features.components.atoms.textfield

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor

@Composable
fun TextArea(
    value: String?,
    onValueChange: (String?) -> Unit,
    supportingText: String? = null,
    modifier: Modifier = Modifier,
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
        )
    }
)

@Composable
private fun TextAreaDecorationBox(
    text: String?,
    supportingText: String?,
    innerTextField: @Composable () -> Unit,
) = Box {
    if (text.isNullOrBlank() && !supportingText.isNullOrBlank()) {
        Text(
            supportingText,
            style = LocalTextStyle.current.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
        )
    }

    innerTextField()
}
