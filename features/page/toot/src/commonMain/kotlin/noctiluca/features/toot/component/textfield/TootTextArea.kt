package noctiluca.features.toot.component.textfield

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import noctiluca.features.shared.components.textfield.TextArea
import noctiluca.features.shared.getCommonString

internal val TootAreaPadding = 16.dp
private val OptionButtonsHorizontalPadding = 4.dp

private val LocalEnabledTootTextArea = compositionLocalOf { true }

@Composable
internal fun TootTextArea(
    content: String,
    warning: String? = null,
    isContentWarning: Boolean,
    enabled: Boolean,
    borderColor: Color = DividerDefaults.color,
    onChangeContent: (String?) -> Unit,
    onChangeWarningText: (String?) -> Unit,
) {
    WarningTextField(
        warning,
        isContentWarning,
        enabled,
        borderColor,
        onChangeWarningText,
    )

    TextArea(
        content,
        enabled = enabled,
        onValueChange = { onChangeContent(it) },
        supportingText = getCommonString().toot_support_text,
        modifier = Modifier.fillMaxWidth()
            .padding(TootAreaPadding),
    )
}

@Composable
private fun WarningTextField(
    warning: String?,
    isContentWarning: Boolean,
    enabled: Boolean,
    borderColor: Color = MaterialTheme.colorScheme.outline,
    onValueChange: (String?) -> Unit,
) {
    if (!isContentWarning) {
        return
    }

    TextArea(
        warning ?: "",
        enabled = enabled,
        onValueChange = { onValueChange(it) },
        supportingText = getCommonString().toot_warning_support_text,
        modifier = Modifier.fillMaxWidth()
            .padding(TootAreaPadding),
    )

    HorizontalDivider(
        color = borderColor,
        modifier = Modifier.padding(horizontal = TootAreaPadding),
    )
}
