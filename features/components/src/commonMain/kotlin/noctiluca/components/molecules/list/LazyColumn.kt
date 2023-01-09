package noctiluca.components.molecules.list

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noctiluca.components.atoms.divider.Divider
import androidx.compose.foundation.lazy.LazyColumn as ComposeLazyColumn

@Composable
fun <T> LazyColumn(
    items: List<T>,
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
    itemsIndexed(items) { index, item ->
        listContent(index, item)

        if (showDivider && index < items.lastIndex) {
            Divider()
        }
    }
}
