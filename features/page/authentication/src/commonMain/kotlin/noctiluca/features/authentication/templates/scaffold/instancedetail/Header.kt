package noctiluca.features.authentication.templates.scaffold.instancedetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import noctiluca.features.components.atoms.image.AsyncImage
import noctiluca.features.components.atoms.text.HtmlText
import noctiluca.model.Uri
import noctiluca.model.authentication.Instance

@Suppress("FunctionNaming")
fun LazyListScope.InstanceDetailHeader(
    instance: Instance,
    tabs: @Composable () -> Unit,
    horizontalPadding: Dp,
) {
    item { InstanceThumbnail(instance.thumbnail, horizontalPadding) }
    item { InstanceName(instance, horizontalPadding) }
    item { InstanceDescription(instance, horizontalPadding) }
    item { tabs() }
}

@Composable
private fun InstanceThumbnail(
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
private fun InstanceName(
    instance: Instance,
    horizontalPadding: Dp,
) = Column(
    modifier = Modifier.fillMaxWidth()
        .padding(
            vertical = 16.dp,
            horizontal = horizontalPadding,
        ),
) {
    Text(
        instance.name,
        style = MaterialTheme.typography.headlineSmall,
    )

    Text(
        instance.domain,
        color = MaterialTheme.colorScheme.outline,
        style = MaterialTheme.typography.bodyLarge,
    )
}

@Composable
private fun InstanceDescription(
    instance: Instance,
    horizontalPadding: Dp,
) = HtmlText(
    instance.description ?: "",
    style = MaterialTheme.typography.bodyLarge,
    modifier = Modifier.fillMaxWidth()
        .padding(
            bottom = 16.dp,
            start = horizontalPadding,
            end = horizontalPadding,
        ),
)
