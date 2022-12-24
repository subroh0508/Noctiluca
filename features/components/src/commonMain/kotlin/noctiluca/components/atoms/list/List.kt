package noctiluca.components.atoms.list

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import noctiluca.model.Uri
import androidx.compose.material3.ListItem as MaterialListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListItem(
    headlineText: String,
    modifier: Modifier = Modifier,
    supportingText: String? = null,
    leadingContent: (@Composable () -> Unit)? = null,
) = MaterialListItem(
    { Text(headlineText) },
    modifier,
    supportingText = supportingText?.let { { Text(it) } },
    leadingContent = leadingContent,
)
