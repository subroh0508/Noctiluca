package noctiluca.features.shared.molecules.list

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import noctiluca.features.shared.atoms.divider.Divider
import androidx.compose.foundation.lazy.LazyColumn as ComposeLazyColumn

val InfiniteScrollFooterHeight = 64.dp

@Composable
fun <T> LazyColumn(
    items: List<T>,
    key: (T) -> String,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    showDivider: Boolean = false,
    footerContent: (@Composable LazyItemScope.() -> Unit)? = null,
    listContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit,
) = ComposeLazyColumn(
    modifier,
    state,
    contentPadding,
    reverseLayout,
    verticalArrangement,
    horizontalAlignment,
    flingBehavior,
    userScrollEnabled,
) {
    itemsIndexed(items, key = { _, item -> key(item) }) { index, item ->
        listContent(index, item)

        if (showDivider && index < items.lastIndex) {
            Divider(color = MaterialTheme.colorScheme.outline)
        }
    }

    footerContent?.let { footer(it) }
}

@Composable
fun LazyColumn(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: LazyListScope.() -> Unit
) = ComposeLazyColumn(
    modifier,
    state,
    contentPadding,
    content = content,
)

@Composable
fun InfiniteScrollFooter(
    isLoading: Boolean,
    onLoad: suspend CoroutineScope.() -> Unit,
    height: Dp = InfiniteScrollFooterHeight,
) {
    LaunchedEffect(Unit) { onLoad() }

    if (!isLoading) {
        Spacer(Modifier.height(height))
        return
    }

    Box(
        modifier = Modifier.fillMaxWidth()
            .height(height),
    ) { CircularProgressIndicator(Modifier.align(Alignment.Center)) }
}

inline fun <T : Any> LazyListScope.items(
    items: List<T>,
    noinline key: ((index: Int, item: T) -> Any)? = null,
    showDivider: Boolean = false,
    crossinline contentType: (index: Int, item: T) -> Any? = { _, _ -> null },
    crossinline itemContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit
) = itemsIndexed(
    items,
    key,
    contentType,
) { i, item ->
    if (showDivider && i < items.lastIndex) {
        Divider(color = MaterialTheme.colorScheme.outline)
    }

    itemContent(i, item)
}

inline fun LazyListScope.footer(
    crossinline content: @Composable LazyItemScope.() -> Unit,
) = item {
    Divider(color = MaterialTheme.colorScheme.outline)
    content()
}

fun LazyListScope.infiniteScrollFooter(
    isLoading: Boolean,
    onLoad: suspend CoroutineScope.() -> Unit,
) = footer { InfiniteScrollFooter(isLoading, onLoad) }
