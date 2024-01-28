package noctiluca.features.accountdetail.component.headline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import noctiluca.features.shared.atoms.image.AsyncImage
import noctiluca.model.Uri

private val AvatarIconSize = 96.dp
private val AvatarFrameSize = 104.dp

private val HeadlinedScaffoldHorizontalPadding = 16.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Avatar(
    avatar: Uri?,
    scrollBehavior: TopAppBarScrollBehavior,
) = Box(
    modifier = Modifier.size(AvatarFrameSize)
        .offset(
            x = HeadlinedScaffoldHorizontalPadding,
            y = ExpandedTopAppBarHeight - AvatarFrameSize / 2,
        )
        .graphicsLayer {
            translationY = calculateTranslationY(scrollBehavior)
            alpha = 1.0F - scrollBehavior.state.collapsedFraction
        }
        .background(
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(8.dp),
        ),
    contentAlignment = Alignment.Center,
) {
    AsyncImage(
        avatar,
        modifier = Modifier.size(AvatarIconSize)
            .clip(RoundedCornerShape(8.dp)),
    )
}
