package noctiluca.features.authentication.organisms.tab

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import noctiluca.features.authentication.getString
import noctiluca.features.authentication.state.Instances
import noctiluca.features.components.atoms.image.AsyncImage
import noctiluca.features.components.atoms.image.NumberCircle
import noctiluca.features.components.atoms.list.LeadingAvatarContainerSize
import noctiluca.features.components.atoms.list.Section
import noctiluca.features.components.atoms.list.SectionItem
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

@Composable
internal fun InstanceInformation(
    instance: Instance,
) = Column {
    AdministratorSection(instance.administrator)
    RulesSection(instance.rules)
    VersionSection(instance.version)
}

@Composable
private fun AdministratorSection(
    administrator: Instance.Administrator,
) = Section(
    getString().sign_in_instance_detail_info_administrator_label,
) {
    SectionItem(
        headlineText = administrator.displayName,
        supportingText = administrator.screen,
        leadingContent = {
            AsyncImage(
                administrator.avatar,
                modifier = Modifier.size(LeadingAvatarContainerSize)
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
}

@Composable
private fun RulesSection(
    rules: List<Instance.Rule>,
) {
    if (rules.isEmpty()) return

    Section(
        getString().sign_in_instance_detail_info_instance_rule_label,
    ) {
        rules.forEachIndexed { i, rule ->
            SectionItem(
                headlineText = rule.text,
                leadingContent = { NumberCircle(i + 1) },
                modifier = Modifier.padding(vertical = 8.dp),
            )
        }
    }
}

@Composable
private fun VersionSection(
    version: Instance.Version?,
) {
    version ?: return

    Section(
        getString().sign_in_instance_detail_info_instance_version_label,
    ) {
        SectionItem(headlineText = "v$version")
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
