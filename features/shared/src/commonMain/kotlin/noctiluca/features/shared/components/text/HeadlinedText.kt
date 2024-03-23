package noctiluca.features.shared.components.text

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun HeadlineText(
    text: String,
    supportingText: String,
    isHiddenHeadlineText: Boolean,
) {
    if (!isHiddenHeadlineText) {
        return
    }

    Column {
        Text(
            text,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Medium,
            ),
        )
        Text(
            supportingText,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Normal,
            ),
        )
    }
}
