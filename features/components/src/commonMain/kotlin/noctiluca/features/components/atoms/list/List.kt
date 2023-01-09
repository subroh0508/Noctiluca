package noctiluca.features.components.atoms.list

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ListItem as MaterialListItem

val LeadingAvatarContainerSize = 40.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OneLineListItem(
    headlineText: String,
    modifier: Modifier = Modifier,
    leadingContent: (@Composable () -> Unit)? = null,
) = MaterialListItem(
    { Text(headlineText) },
    modifier,
    leadingContent = leadingContent,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TwoLineListItem(
    headlineText: String,
    modifier: Modifier = Modifier,
    supportingText: String,
    leadingContent: (@Composable () -> Unit)? = null,
) = MaterialListItem(
    { Text(headlineText) },
    modifier,
    supportingText = { Text(supportingText) },
    leadingContent = leadingContent,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThreeLineListItem(
    headlineText: String,
    modifier: Modifier = Modifier,
    supportingText: String,
    leadingContent: (@Composable () -> Unit)? = null,
) = MaterialListItem(
    { Text(headlineText) },
    modifier,
    supportingText = { Text(supportingText) },
    overlineText = {},
    leadingContent = leadingContent,
)
