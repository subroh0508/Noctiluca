package noctiluca.features.components.atoms.card

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FilledCard(
    headline: @Composable ColumnScope.() -> Unit,
    subhead: (@Composable ColumnScope.() -> Unit)? = null,
    supporting: (@Composable ColumnScope.() -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
    modifier: Modifier = Modifier,
) = Card(modifier) {
    Column(Modifier.padding(16.dp)) {
        headline()
        subhead?.let {
            Spacer(Modifier.height(8.dp))
            it.invoke(this)
        }
        supporting?.let {
            Spacer(Modifier.height(32.dp))
            it.invoke(this)
        }
        actions?.let {
            Spacer(Modifier.height(32.dp))
            CardActions(it)
        }
    }
}

@Composable
fun FilledCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) = Card(modifier) { content() }
