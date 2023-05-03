package noctiluca.features.authentication.organisms.card

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import noctiluca.features.components.atoms.image.AsyncImage
import noctiluca.instance.model.Instance

@Composable
internal fun SuggestCard(
    suggest: Instance.Suggest,
    onSelect: (Instance.Suggest) -> Unit,
    modifier: Modifier = Modifier,
) = Box(
    modifier = Modifier.fillMaxWidth()
        .heightIn(min = 72.dp, max = 160.dp)
        .then(modifier)
        .clip(RoundedCornerShape(CornerSize(12.dp)))
        .background(MaterialTheme.colorScheme.surfaceVariant),
) {
    AsyncImage(
        suggest.thumbnail,
        contentScale = ContentScale.FillWidth,
        modifier = Modifier.fillMaxSize(),
    )

    Column(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.75F))
            .clickable { onSelect(suggest) }
            .padding(16.dp),
    ) {
        Text(
            text = suggest.domain,
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = suggest.description ?: "",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
