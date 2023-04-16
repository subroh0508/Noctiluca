package noctiluca.features.components.molecules.list

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noctiluca.features.components.atoms.divider.Divider
import androidx.compose.foundation.lazy.LazyColumn as ComposeLazyColumn

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

    footerContent?.let { footer ->
        item {
            Divider(color = MaterialTheme.colorScheme.outline)
            footer()
        }
    }
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
