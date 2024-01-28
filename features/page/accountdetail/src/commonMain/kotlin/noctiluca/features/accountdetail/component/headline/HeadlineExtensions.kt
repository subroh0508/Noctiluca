package noctiluca.features.accountdetail.component.headline

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.unit.dp

internal val ExpandedTopAppBarHeight = 152.dp

private val CollapsedTopAppBarHeight = 64.dp
private val HeaderHeightOffset = -(ExpandedTopAppBarHeight - CollapsedTopAppBarHeight)

@OptIn(ExperimentalMaterial3Api::class)
internal fun GraphicsLayerScope.calculateTranslationY(
    scrollBehavior: TopAppBarScrollBehavior,
): Float {
    val offset = HeaderHeightOffset.toPx()
    return if (offset < scrollBehavior.state.heightOffset) scrollBehavior.state.heightOffset else offset
}
