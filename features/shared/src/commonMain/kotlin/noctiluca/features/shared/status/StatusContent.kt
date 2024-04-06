package noctiluca.features.shared.status

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import noctiluca.features.shared.components.text.HtmlText
import noctiluca.features.shared.getCommonString

@Composable
fun StatusContent(
    content: String,
    warningText: String?,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
) {
    if (warningText.isNullOrBlank()) {
        HtmlText(
            content,
            style = style,
        )
        return
    }

    StatusContentWithWarning(
        content,
        warningText,
        style,
    )
}

@Composable
private fun StatusContentWithWarning(
    content: String,
    warningText: String,
    style: TextStyle,
) = Column(
    modifier = Modifier.fillMaxWidth(),
) {
    var showContent by remember { mutableStateOf(false) }

    HtmlText(
        warningText,
        style = style,
    )

    SuggestionChip(
        onClick = { showContent = !showContent },
        label = {
            Text(
                if (showContent) {
                    getCommonString().toot_hide_content
                } else {
                    getCommonString().toot_show_content
                },
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        },
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        border = null,
        modifier = Modifier.align(Alignment.End),
    )

    if (showContent) {
        HtmlText(
            content,
            style = style,
        )
    }
}
