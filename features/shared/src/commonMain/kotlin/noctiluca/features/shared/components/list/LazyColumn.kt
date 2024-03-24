package noctiluca.features.shared.components.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope

val InfiniteScrollFooterHeight = 64.dp

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
        HorizontalDivider()
    }

    itemContent(i, item)
}

inline fun LazyListScope.footer(
    crossinline content: @Composable LazyItemScope.() -> Unit,
) = item {
    HorizontalDivider()
    content()
}

fun LazyListScope.infiniteScrollFooter(
    isLoading: Boolean,
    onLoad: suspend CoroutineScope.() -> Unit,
    content: @Composable (Dp) -> Unit = { dp -> Spacer(Modifier.height(dp)) },
) = footer { InfiniteScrollFooter(isLoading, onLoad, content) }
