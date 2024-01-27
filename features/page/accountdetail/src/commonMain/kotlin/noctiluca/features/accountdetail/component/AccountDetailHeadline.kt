package noctiluca.features.accountdetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import noctiluca.features.accountdetail.getString
import noctiluca.features.shared.atoms.image.AsyncImage
import noctiluca.model.Uri
import noctiluca.model.accountdetail.AccountAttributes
import noctiluca.model.accountdetail.Relationships

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
        attributes,
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
    attributes: AccountAttributes,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    val modifier = Modifier.height(ExpandedTopAppBarHeight)
        .fillMaxWidth()
        .graphicsLayer {
            translationY = calculateTranslationY(scrollBehavior)
        }

    Box(modifier) {
        if (attributes.header == null) {
            Spacer(
                modifier = Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface),
            )
        } else {
            AsyncImage(
                attributes.header,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }

        RelationshipStateChips(
            relationships = attributes.relationships,
            modifier = Modifier.align(Alignment.BottomEnd)
                .graphicsLayer {
                    alpha = 1.0F - scrollBehavior.state.collapsedFraction
                },
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

@Composable
private fun RelationshipStateChips(
    relationships: Relationships,
    modifier: Modifier = Modifier,
) = Column(
    horizontalAlignment = Alignment.End,
    modifier = modifier.padding(bottom = 8.dp, end = 16.dp),
) {
    if (relationships.muting) {
        RelationshipStateChip(Icons.Default.Error, getString().account_detail_muting)
    }

    relationships.assets()?.let { (label, icon) ->
        Spacer(modifier = Modifier.height(4.dp))
        RelationshipStateChip(icon, label)
    }
}

@Composable
private fun RelationshipStateChip(
    imageVector: ImageVector,
    label: String,
) = Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier.height(AssistChipDefaults.Height)
        .background(
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8F),
            shape = RoundedCornerShape(8.dp),
        )
        .padding(start = 8.dp, end = 16.dp),
) {
    Icon(
        imageVector,
        contentDescription = null,
        modifier = Modifier.size(18.dp),
    )
    Spacer(modifier = Modifier.width(8.dp))
    Text(
        label,
        style = MaterialTheme.typography.labelLarge,
    )
}

@Composable
private fun Relationships.assets() = when {
    followedBy -> getString().account_detail_followed to Icons.Default.Check
    requested -> getString().account_detail_sending_follow_request to Icons.AutoMirrored.Filled.Send
    blocking && !blockedBy -> getString().account_detail_blocking to Icons.Default.Block
    !blocking && blockedBy -> getString().account_detail_blocked to Icons.Default.Block
    else -> null
}

@OptIn(ExperimentalMaterial3Api::class)
private fun GraphicsLayerScope.calculateTranslationY(
    scrollBehavior: TopAppBarScrollBehavior,
): Float {
    val offset = HeaderHeightOffset.toPx()
    return if (offset < scrollBehavior.state.heightOffset) scrollBehavior.state.heightOffset else offset
}
