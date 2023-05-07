package noctiluca.features.components.atoms.tab

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val TabIndicatorHeight = 3.dp
private val TabIndicatorShape = 3.dp

@Composable
fun <T : Any> PrimaryTabs(
    tabs: List<T>,
    selectedTabIndex: Int,
    onClick: (Int, T) -> Unit,
    transform: @Composable (T) -> String = { it.toString() },
    modifier: Modifier = Modifier,
) {
    val tabTextWidth = remember(tabs.size) {
        mutableStateListOf<Dp>().also { list ->
            repeat(tabs.size) { list.add(0.dp) }
        }
    }

    val indicator: @Composable (tabPositions: List<TabPosition>) -> Unit = @Composable { tabPositions ->
        PrimaryTabIndicator(
            tabPositions[selectedTabIndex],
            tabTextWidth[selectedTabIndex],
        )
    }

    TabRow(
        selectedTabIndex,
        modifier = modifier,
        indicator = indicator,
    ) {
        tabs.forEachIndexed { index, tab ->
            Tab(
                selected = index == selectedTabIndex,
                onClick = { onClick(index, tab) },
                text = {
                    TabText(
                        transform(tab),
                        index,
                        tabTextWidth,
                    )
                },
            )
        }
    }
}

@Composable
private fun PrimaryTabIndicator(
    currentTabPosition: TabPosition,
    tabTextWidth: Dp,
) = Box(
    Modifier.composed(
        inspectorInfo = debugInspectorInfo {
            name = "tabIndicatorOffset"
            value = currentTabPosition
        }
    ) {
        val currentTabWidth by animateDpAsState(
            targetValue = tabTextWidth,
            animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing)
        )
        val indicatorOffset by animateDpAsState(
            targetValue = ((currentTabPosition.left + currentTabPosition.right) - currentTabWidth) / 2,
            animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing),
        )
        fillMaxWidth()
            .wrapContentSize(Alignment.BottomStart)
            .offset(x = indicatorOffset)
            .width(currentTabWidth)
    }
        .fillMaxWidth()
        .height(TabIndicatorHeight)
        .background(MaterialTheme.colorScheme.primary)
        .clip(RoundedCornerShape(TabIndicatorShape, TabIndicatorShape, 0.dp, 0.dp)),
)

@Composable
private fun TabText(
    text: String,
    tabIndex: Int,
    tabWidth: SnapshotStateList<Dp>,
) {
    val density = LocalDensity.current

    Text(
        text,
        onTextLayout = { textLayoutResult ->
            tabWidth[tabIndex] = with(density) {
                textLayoutResult.size.width.toDp()
            }
        },
    )
}
