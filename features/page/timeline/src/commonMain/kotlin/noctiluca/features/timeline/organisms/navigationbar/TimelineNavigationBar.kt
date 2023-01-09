package noctiluca.features.timeline.organisms.navigationbar

import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import noctiluca.features.timeline.LocalTimelineListState
import noctiluca.features.timeline.getString
import noctiluca.timeline.domain.model.Timeline

@Composable
internal fun TimelineNavigationBar() {
    val timeline = LocalTimelineListState.current

    NavigationBar {
        timeline.value.forEachIndexed { index, (value, _, foreground) ->
            NavigationBarItem(
                icon = { Icon(value.icon, contentDescription = value.label()) },
                label = { Text(value.label()) },
                selected = foreground,
                onClick = {},
            )
        }
    }
}

private val Timeline.icon get() = when(this) {
    is Timeline.Global -> Icons.Default.Public
    is Timeline.Local -> Icons.Default.Group
    is Timeline.Home -> Icons.Default.Home
    is Timeline.HashTag -> Icons.Default.Tag
    is Timeline.List -> Icons.Default.List
}

@Composable
private fun Timeline.label() = when(this) {
    is Timeline.Global -> getString().timeline_global
    is Timeline.Local -> getString().timeline_local
    is Timeline.Home -> getString().timeline_home
    is Timeline.HashTag -> hashtag.value
    is Timeline.List -> list.title
}
