package noctiluca.features.signin.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import noctiluca.features.shared.atoms.image.AsyncImage
import noctiluca.features.shared.atoms.text.HtmlText
import noctiluca.model.Uri

@Composable
internal fun InstanceThumbnail(
    thumbnail: Uri?,
    horizontalPadding: Dp,
) = Box(
    Modifier.padding(
        top = 8.dp,
        start = horizontalPadding,
        end = horizontalPadding,
    )
) {
    AsyncImage(
        thumbnail,
        contentScale = ContentScale.FillWidth,
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),
    )
}

@Composable
internal fun InstanceName(
    name: String,
    domain: String,
    horizontalPadding: Dp,
) = Column(
    modifier = Modifier.fillMaxWidth()
        .padding(
            vertical = 16.dp,
            horizontal = horizontalPadding,
        ),
) {
    Text(
        name,
        style = MaterialTheme.typography.headlineSmall,
    )

    Text(
        domain,
        color = MaterialTheme.colorScheme.outline,
        style = MaterialTheme.typography.bodyLarge,
    )
}

@Composable
internal fun InstanceDescription(
    description: String?,
    horizontalPadding: Dp,
) = HtmlText(
    description ?: "",
    style = MaterialTheme.typography.bodyLarge,
    modifier = Modifier.fillMaxWidth()
        .padding(
            bottom = 16.dp,
            start = horizontalPadding,
            end = horizontalPadding,
        ),
)
