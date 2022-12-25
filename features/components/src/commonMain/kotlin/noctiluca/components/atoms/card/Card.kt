package noctiluca.components.atoms.card

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CardHeader(text: String) = Text(
    text,
    style = MaterialTheme.typography.headlineSmall,
)

@Composable
fun CardSubhead(text: String) = Text(
    text,
    style = MaterialTheme.typography.bodyLarge,
)

@Composable
fun CardSupporting(text: String) = Text(
    text,
    style = MaterialTheme.typography.bodyMedium,
)

@Composable
internal fun CardActions(
    actions: @Composable RowScope.() -> Unit,
) = Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.End,
) { actions() }
