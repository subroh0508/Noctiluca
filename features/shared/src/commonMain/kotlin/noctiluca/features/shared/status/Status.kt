package noctiluca.features.shared.status

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noctiluca.components.atoms.image.AsyncImage
import noctiluca.components.atoms.text.HtmlText
import noctiluca.status.model.Status

private val TooterIconSize = 40.dp

@Composable
fun Status(
    status: Status,
    modifier: Modifier = Modifier,
) = Row(modifier) {
    AsyncImage(
        status.tooter.avatar,
        //fallback = imageResources(getDrawables().icon_mastodon),
        modifier = Modifier.size(TooterIconSize),
    )

    Column {
        Text(
            status.tooter.displayName,
            style = MaterialTheme.typography.titleMedium,
        )

        HtmlText(
            status.content,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}
