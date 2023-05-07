package noctiluca.features.components.atoms.textfield

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import noctiluca.features.components.atoms.clickable

@Composable
fun TextArea(
    value: String?,
    onValueChange: (String?) -> Unit,
    onClickClear: () -> Unit = {},
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
            onClickClear,
        )
    }
)

@Composable
private fun TextAreaDecorationBox(
    text: String?,
    supportingText: String?,
    innerTextField: @Composable () -> Unit,
    onClickClear: () -> Unit,
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
        Spacer(Modifier.width(16.dp))
        Icon(
            Icons.Default.Cancel,
            contentDescription = "Clear",
            modifier = Modifier.size(24.dp)
                .align(Alignment.Top)
                .clickable(
                    noRipple = true,
                    onClick = onClickClear,
                ),
        )
    }
}
