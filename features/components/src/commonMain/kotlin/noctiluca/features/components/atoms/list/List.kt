package noctiluca.features.components.atoms.list

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ListItem as MaterialListItem

val LeadingAvatarContainerSize = 40.dp

val SectionPadding = 16.dp

@Composable
fun Section(
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) = Column {
    Spacer(Modifier.height(SectionPadding))

    Text(
        title,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier.padding(horizontal = SectionPadding),
    )

    content()

    Divider(Modifier.fillMaxWidth())
}

@Composable
fun SectionItem(
    headlineText: String,
    supportingText: String? = null,
    modifier: Modifier = Modifier,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
) = MaterialListItem(
    { Text(headlineText) },
    modifier,
    supportingContent = supportingText?.let { { Text(it) } },
    leadingContent = leadingContent,
    trailingContent = trailingContent,
)

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

@Composable
fun TwoLineListItem(
    headlineText: String,
    modifier: Modifier = Modifier,
    supportingText: String,
    leadingContent: (@Composable () -> Unit)? = null,
) = MaterialListItem(
    { Text(headlineText) },
    modifier,
    supportingContent = { Text(supportingText) },
    leadingContent = leadingContent,
)

@Composable
fun ThreeLineListItem(
    headlineText: String,
    modifier: Modifier = Modifier,
    supportingText: String,
    leadingContent: (@Composable () -> Unit)? = null,
) = MaterialListItem(
    { Text(headlineText) },
    modifier,
    supportingContent = { Text(supportingText) },
    overlineContent = {},
    leadingContent = leadingContent,
)
