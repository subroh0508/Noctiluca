package noctiluca.features.components.molecules.scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import noctiluca.features.components.atoms.image.AsyncImage
import noctiluca.features.components.molecules.list.LazyColumn
import noctiluca.model.Uri

private val ExpandedTopAppBarHeight = 152.dp
private val CollapsedTopAppBarHeight = 64.dp

private val HeaderHeightOffset = -(ExpandedTopAppBarHeight - CollapsedTopAppBarHeight)

private val AvatarFrameSize = 104.dp
private val AvatarIconSize = 96.dp

private val HeadlinedScaffoldHorizontalPadding = 16.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeadlinedScaffold(
    lazyListState: LazyListState,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
    topAppBar: @Composable (TopAppBarScrollBehavior) -> Unit = {},
    header: @Composable (TopAppBarScrollBehavior) -> Unit = {},
    avatar: (@Composable (TopAppBarScrollBehavior) -> Unit)? = null,
    tabs: @Composable () -> Unit = {},
    content: LazyListScope.(@Composable () -> Unit, Dp) -> Unit,
) = Scaffold(
    topBar = {
        topAppBar(scrollBehavior)
    },
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
) { paddingValues ->
    LazyColumn(
        state = lazyListState,
        contentPadding = paddingValues,
        modifier = Modifier.fillMaxSize(),
    ) { content(tabs, HeadlinedScaffoldHorizontalPadding) }

    header(scrollBehavior)
    avatar?.let { it(scrollBehavior) }

    if (lazyListState.firstVisibleItemIndex > 0) {
        Box(
            modifier = Modifier.offset(y = CollapsedTopAppBarHeight),
        ) {
            tabs()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeadlineHeader(
    header: Uri?,
    scrollBehavior: TopAppBarScrollBehavior,
) = AsyncImage(
    header,
    ContentScale.FillHeight,
    modifier = Modifier.height(ExpandedTopAppBarHeight)
        .graphicsLayer {
            translationY = calculateTranslationY(scrollBehavior)
        },
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeadlineAvatar(
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
