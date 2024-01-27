package noctiluca.features.accountdetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import noctiluca.features.shared.atoms.image.AsyncImage
import noctiluca.model.Uri
import noctiluca.model.accountdetail.AccountAttributes

private val AvatarFrameSize = 104.dp

private val ExpandedTopAppBarHeight = 152.dp
private val CollapsedTopAppBarHeight = 64.dp

private val HeaderHeightOffset = -(ExpandedTopAppBarHeight - CollapsedTopAppBarHeight)

private val AvatarIconSize = 96.dp

private val HeadlinedScaffoldHorizontalPadding = 16.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AccountDetailHeadline(
    attributes: AccountAttributes,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    HeadlineHeader(
        attributes.header,
        scrollBehavior,
    )

    HeadlineAvatar(
        attributes.avatar,
        scrollBehavior,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HeadlineHeader(
    header: Uri?,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    val modifier = Modifier.height(ExpandedTopAppBarHeight)
        .fillMaxWidth()
        .graphicsLayer {
            translationY = calculateTranslationY(scrollBehavior)
        }

    if (header == null) {
        Spacer(
            modifier = modifier.background(MaterialTheme.colorScheme.surface),
        )
    } else {
        AsyncImage(
            header,
            contentScale = ContentScale.FillHeight,
            modifier = modifier,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HeadlineAvatar(
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

@OptIn(ExperimentalMaterial3Api::class)
private fun GraphicsLayerScope.calculateTranslationY(
    scrollBehavior: TopAppBarScrollBehavior,
): Float {
    val offset = HeaderHeightOffset.toPx()
    return if (offset < scrollBehavior.state.heightOffset) scrollBehavior.state.heightOffset else offset
}
