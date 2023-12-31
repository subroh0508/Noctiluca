package noctiluca.features.shared.molecules.scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import noctiluca.features.shared.LoadStateComposable
import noctiluca.features.shared.atoms.image.AsyncImage
import noctiluca.model.LoadState
import noctiluca.features.shared.molecules.list.LazyColumn
import noctiluca.model.Uri

private val ExpandedTopAppBarHeight = 152.dp
private val CollapsedTopAppBarHeight = 64.dp

private val HeaderHeightOffset = -(ExpandedTopAppBarHeight - CollapsedTopAppBarHeight)

private val AvatarFrameSize = 104.dp
private val AvatarIconSize = 96.dp

private val HeadlinedScaffoldHorizontalPadding = 16.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Any> LoadStateLargeHeadlinedScaffold(
    loadState: LoadState,
    lazyListState: LazyListState,
    tabComposeIndex: Int = Int.MAX_VALUE,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    topAppBar: @Composable (TopAppBarScrollBehavior, Job?, T?) -> Unit = { _, _, _ -> },
    header: @Composable (TopAppBarScrollBehavior, T) -> Unit = { _, _ -> },
    avatar: (@Composable (TopAppBarScrollBehavior, T) -> Unit) = { _, _ -> },
    bottomBar: @Composable BoxScope.(T, Dp) -> Unit = { _, _ -> },
    tabs: @Composable () -> Unit = {},
    fallback: @Composable (Throwable?, PaddingValues) -> Unit = { _, _ -> },
    content: LazyListScope.(T, @Composable () -> Unit, Dp) -> Unit,
) = LoadStateHeadlinedScaffold(
    loadState,
    lazyListState,
    tabComposeIndex,
    TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
    snackbarHostState,
    topAppBar,
    header,
    avatar,
    bottomBar,
    tabs,
    fallback,
    content,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Any> LoadStateSmallHeadlinedScaffold(
    loadState: LoadState,
    lazyListState: LazyListState,
    tabComposeIndex: Int = Int.MAX_VALUE,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    topAppBar: @Composable (TopAppBarScrollBehavior, Job?, T?) -> Unit = { _, _, _ -> },
    bottomBar: @Composable BoxScope.(T, Dp) -> Unit = { _, _ -> },
    tabs: @Composable () -> Unit = {},
    fallback: @Composable (Throwable?, PaddingValues) -> Unit = { _, _ -> },
    content: LazyListScope.(T, @Composable () -> Unit, Dp) -> Unit,
) = LoadStateHeadlinedScaffold(
    loadState,
    lazyListState,
    tabComposeIndex,
    TopAppBarDefaults.pinnedScrollBehavior(),
    snackbarHostState,
    topAppBar,
    bottomBar = bottomBar,
    tabs = tabs,
    fallback = fallback,
    content = content,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T : Any> LoadStateHeadlinedScaffold(
    loadState: LoadState,
    lazyListState: LazyListState,
    tabComposeIndex: Int = Int.MAX_VALUE,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    topAppBar: @Composable (TopAppBarScrollBehavior, Job?, T?) -> Unit = { _, _, _ -> },
    header: @Composable (TopAppBarScrollBehavior, T) -> Unit = { _, _ -> },
    avatar: (@Composable (TopAppBarScrollBehavior, T) -> Unit) = { _, _ -> },
    bottomBar: @Composable BoxScope.(T, Dp) -> Unit = { _, _ -> },
    tabs: @Composable () -> Unit = {},
    fallback: @Composable (Throwable?, PaddingValues) -> Unit = { _, _ -> },
    content: LazyListScope.(T, @Composable () -> Unit, Dp) -> Unit,
) = Scaffold(
    topBar = {
        topAppBar(
            scrollBehavior,
            (loadState as? LoadState.Loading)?.job,
            loadState.getValueOrNull(),
        )
    },
    snackbarHost = { SnackbarHost(snackbarHostState) },
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
) { paddingValues ->
    LoadStateComposable<T>(
        loadState,
        loading = {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
                    .offset(y = paddingValues.calculateTopPadding()),
            )
        },
        fallback = { error ->
            fallback(
                error,
                PaddingValues(
                    top = paddingValues.calculateTopPadding(),
                    start = HeadlinedScaffoldHorizontalPadding,
                    end = HeadlinedScaffoldHorizontalPadding,
                ),
            )
        },
    ) { data ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                state = lazyListState,
                contentPadding = PaddingValues(
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                    top = paddingValues.calculateTopPadding(),
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
                    bottom = 64.dp,
                ),
                modifier = Modifier.fillMaxSize(),
            ) {
                content(data, tabs, HeadlinedScaffoldHorizontalPadding)
            }

            header(scrollBehavior, data)
            avatar(scrollBehavior, data)

            if (lazyListState.firstVisibleItemIndex >= tabComposeIndex) {
                Box(
                    modifier = Modifier.offset(y = CollapsedTopAppBarHeight),
                ) { tabs() }
            }

            bottomBar(data, HeadlinedScaffoldHorizontalPadding)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeadlineTopAppBar(
    title: @Composable (TopAppBarState) -> Unit,
    onBackPressed: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
) = TopAppBar(
    { title(scrollBehavior.state) },
    navigationIcon = {
        IconButton(onClick = onBackPressed) {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = "Back",
            )
        }
    },
    actions = actions,
    colors = TopAppBarDefaults.smallTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.surface,
        scrolledContainerColor = MaterialTheme.colorScheme.surface,
    ),
    scrollBehavior = scrollBehavior,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LargeHeadlineTopAppBar(
    title: @Composable (TopAppBarState) -> Unit,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
) {
    val alpha by rememberScrolledContainerColorAlpha(scrollBehavior)

    LargeTopAppBar(
        { title(scrollBehavior.state) },
        modifier = modifier.background(
            Brush.verticalGradient(
                colors = listOf(
                    Color.Black.copy(alpha = 0.75F),
                    Color.Black.copy(alpha = alpha),
                ),
            ),
        ),
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                )
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent,
        ),
        scrollBehavior = scrollBehavior,
    )
}

@Composable
fun HeadlineText(
    text: String,
    supportingText: String,
    isHiddenHeadlineText: Boolean,
) {
    if (!isHiddenHeadlineText) {
        return
    }

    Column {
        Text(
            text,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Medium,
            ),
        )
        Text(
            supportingText,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Normal,
            ),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeadlineHeader(
    header: Uri?,
    scrollBehavior: TopAppBarScrollBehavior,
    contentScale: ContentScale = ContentScale.FillHeight
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
            contentScale = contentScale,
            modifier = modifier,
        )
    }
}

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
@Composable
@Suppress("MagicNumber")
private fun rememberScrolledContainerColorAlpha(
    scrollBehavior: TopAppBarScrollBehavior,
): State<Float> = remember {
    derivedStateOf { scrollBehavior.state.collapsedFraction * 0.75F }
}

@OptIn(ExperimentalMaterial3Api::class)
private fun GraphicsLayerScope.calculateTranslationY(
    scrollBehavior: TopAppBarScrollBehavior,
): Float {
    val offset = HeaderHeightOffset.toPx()
    return if (offset < scrollBehavior.state.heightOffset) scrollBehavior.state.heightOffset else offset
}
