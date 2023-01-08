package noctiluca.features.timeline.organisms.list

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import noctiluca.components.atoms.text.HtmlText
import noctiluca.features.timeline.state.rememberTimelineStatus

@Composable
internal fun TimelineLane(
    modifier: Modifier = Modifier,
) {
    val timeline = rememberTimelineStatus()
    val statuses = timeline.findForeground()?.timeline?.statuses ?: listOf()

    LazyColumn(modifier = modifier) {
        items(statuses) {
            HtmlText(it.content)
        }
    }
}
