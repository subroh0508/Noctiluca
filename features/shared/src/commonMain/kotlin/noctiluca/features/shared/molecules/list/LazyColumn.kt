package noctiluca.features.shared.molecules.list

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
    content: @Composable (Dp) -> Unit = { dp -> Spacer(Modifier.height(dp)) },
    height: Dp = InfiniteScrollFooterHeight,
) {
    LaunchedEffect(Unit) { onLoad() }

    if (!isLoading) {
        content(height)
        return
    }

    Box(
        modifier = Modifier.fillMaxWidth()
            .height(height),
    ) { CircularProgressIndicator(Modifier.align(Alignment.Center)) }
}

@Composable
fun EmptyMessage(
    message: String,
    height: Dp,
) = Box(
    modifier = Modifier.fillMaxWidth()
        .height(height),
    contentAlignment = Alignment.Center,
) {
    Text(
        message,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.outline,
    )
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
    content: @Composable (Dp) -> Unit = { dp -> Spacer(Modifier.height(dp)) },
) = footer { InfiniteScrollFooter(isLoading, onLoad, content) }
