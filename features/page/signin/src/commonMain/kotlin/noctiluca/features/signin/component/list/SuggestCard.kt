package noctiluca.features.signin.component.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import noctiluca.features.shared.components.image.AsyncImage
import noctiluca.features.shared.components.text.HtmlText
import noctiluca.model.authorization.Instance

private const val FOREGROUND_ID = "foreground"
private const val BACKGROUND_ID = "background"

@Composable
internal fun SuggestCard(
    suggest: Instance.Suggest,
    onSelect: (Instance.Suggest) -> Unit,
    modifier: Modifier = Modifier,
) = Layout(
    modifier = modifier.clip(RoundedCornerShape(CornerSize(12.dp)))
        .background(MaterialTheme.colorScheme.surfaceVariant),
    content = {
        AsyncImage(
            suggest.thumbnail,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.layoutId(BACKGROUND_ID)
                .fillMaxSize(),
        )

        Column(
            modifier = Modifier.layoutId(FOREGROUND_ID)
                .fillMaxWidth()
                .heightIn(min = 72.dp, max = 160.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.75F))
                .clickable { onSelect(suggest) }
                .padding(16.dp),
        ) {
            Text(
                text = suggest.domain,
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(Modifier.height(8.dp))
            HtmlText(
                text = suggest.description ?: "",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    },
) { measurables, constraints ->
    val backgroundPlaceable = measurables.find {
        it.layoutId == BACKGROUND_ID
    }?.measure(constraints)
    val foregroundPlaceable = measurables.find {
        it.layoutId == FOREGROUND_ID
    }?.measure(constraints)

    layout(constraints.maxWidth, foregroundPlaceable?.height ?: 0) {
        val backgroundY = -((backgroundPlaceable?.height ?: 0) - (foregroundPlaceable?.height ?: 0)) / 2

        backgroundPlaceable?.placeRelative(x = 0, y = backgroundY)
        foregroundPlaceable?.placeRelative(x = 0, y = 0)
    }
}
