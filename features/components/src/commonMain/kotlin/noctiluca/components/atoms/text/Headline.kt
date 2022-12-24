package noctiluca.components.atoms.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HeadlineSmall(
    text: String,
    modifier: Modifier = Modifier,
) = Text(text, style = MaterialTheme.typography.headlineSmall, modifier = modifier)
