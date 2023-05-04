package noctiluca.features.authentication.organisms.tab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.*
import androidx.compose.material3.ListItem
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import noctiluca.features.authentication.getString
import noctiluca.features.authentication.state.Instances
import noctiluca.features.components.atoms.divider.Divider
import noctiluca.features.components.atoms.image.AsyncImage
import noctiluca.instance.model.Instance

@Composable
internal fun InstanceDetailTabs(
    statusesScrollState: InstanceDetailScrollState,
    modifier: Modifier = Modifier,
) = TabRow(
    selectedTabIndex = statusesScrollState.tab.ordinal,
    modifier = modifier,
) {
    statusesScrollState.tabs.forEach { (tab, label) ->
        Tab(
            selected = tab == statusesScrollState.tab,
            onClick = { statusesScrollState.cacheScrollPosition(tab) },
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.height(48.dp),
            ) { Text(label) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun InstanceInformation(
    instance: Instance,
    horizontalPadding: Dp,
) = Column {
    Spacer(Modifier.height(16.dp))

    Text(
        getString().sign_in_instance_detail_info_administrator_label,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier.padding(horizontal = horizontalPadding),
    )
    ListItem(
        headlineText = { Text(instance.administrator.displayName) },
        supportingText = { Text(instance.administrator.screen) },
        leadingContent = {
            AsyncImage(
                instance.administrator.avatar,
                modifier = Modifier.size(40.dp)
                    .clip(RoundedCornerShape(8.dp)),
            )
        },
        trailingContent = {
            IconButton(onClick = { /* TODO */ }) {
                Icon(
                    Icons.Default.Mail,
                    contentDescription = "Mail",
                )
            }
        },
        modifier = Modifier.padding(vertical = 8.dp),
    )

    Divider(Modifier.fillMaxWidth())

    if (instance.rules.isNotEmpty()) {
        Spacer(Modifier.height(16.dp))

        Text(
            getString().sign_in_instance_detail_info_instance_rule_label,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(horizontal = horizontalPadding),
        )

        instance.rules.forEachIndexed { i, rule ->
            ListItem(
                headlineText = { Text(rule.text) },
                leadingContent = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            (i + 1).toString(),
                            color = MaterialTheme.colorScheme.onPrimary,
                            textAlign = TextAlign.Center,
                        )
                    }
                },
                modifier = Modifier.padding(vertical = 8.dp),
            )
        }

        Divider(Modifier.fillMaxWidth())
    }

    instance.version?.toString()?.let { version ->
        Spacer(Modifier.height(16.dp))

        Text(
            getString().sign_in_instance_detail_info_instance_version_label,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(horizontal = horizontalPadding),
        )

        ListItem(headlineText = { Text("v$version") })

        Divider(Modifier.fillMaxWidth())
    }
}

@Composable
internal fun rememberTabbedInstanceDetailState(
    tab: Instances.Tab = Instances.Tab.INFO,
): InstanceDetailScrollState {
    val scrollState = InstanceDetailScrollState(tab)

    LaunchedEffect(tab) { scrollState.restoreScrollPosition(tab) }
    return scrollState
}

internal class InstanceDetailScrollState private constructor(
    val tabs: List<Pair<Instances.Tab, String>>,
    val lazyListState: LazyListState,
    private val currentTab: MutableState<Instances.Tab>,
    private val scrollPositions: MutableState<List<Pair<Int, Int>>>,
) {
    companion object {
        @Composable
        operator fun invoke(
            initTab: Instances.Tab,
            lazyListState: LazyListState = rememberLazyListState(),
        ): InstanceDetailScrollState {
            val tabTitles = listOf(
                Instances.Tab.INFO to getString().sign_in_instance_detail_tab_info,
                Instances.Tab.EXTENDED_DESCRIPTION to getString().sign_in_instance_detail_tab_extended_description,
                Instances.Tab.LOCAL_TIMELINE to getString().sign_in_instance_detail_tab_local_timeline,
            )

            return remember {
                InstanceDetailScrollState(
                    tabTitles,
                    lazyListState,
                    mutableStateOf(initTab),
                    mutableStateOf(List(tabTitles.size) { 1 to 0 }),
                )
            }
        }
    }

    val tab get() = currentTab.value

    fun cacheScrollPosition(next: Instances.Tab) {
        scrollPositions.value = scrollPositions.value.mapIndexed { index, state ->
            if (lazyListState.firstVisibleItemIndex > 0 && index == tab.ordinal) {
                lazyListState.firstVisibleItemIndex to lazyListState.firstVisibleItemScrollOffset
            } else {
                state
            }
        }

        currentTab.value = next
    }

    suspend fun restoreScrollPosition(tab: Instances.Tab) {
        if (lazyListState.firstVisibleItemIndex == 0) {
            return
        }

        val (index, offset) = scrollPositions.value[tab.ordinal]

        lazyListState.scrollToItem(index, offset)
    }
}
