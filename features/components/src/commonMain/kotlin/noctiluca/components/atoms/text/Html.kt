package noctiluca.components.atoms.text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun HtmlText(
    text: String,
    modifier: Modifier = Modifier,
)
