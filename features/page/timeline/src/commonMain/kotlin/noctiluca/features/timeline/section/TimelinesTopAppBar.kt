package noctiluca.features.timeline.section

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import noctiluca.features.shared.atoms.appbar.NavigateIconSize
import noctiluca.features.shared.atoms.image.AsyncImage
import noctiluca.features.timeline.getString
import noctiluca.model.Domain
import noctiluca.model.Uri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TimelinesTopAppBar(
    avatar: Uri?,
    domain: Domain?,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    drawerState: DrawerState,
) {
    val scope = rememberCoroutineScope()

    CenterAlignedTopAppBar(
        { Text(domain?.value ?: getString().timeline_page_title) },
        navigationIcon = {
            IconButton(
                onClick = { scope.launch { drawerState.open() } },
                modifier = Modifier.padding(start = 8.dp),
            ) {
                AsyncImage(
                    avatar,
                    // fallback = imageResources(getDrawables().icon_mastodon),
                    modifier = Modifier.size(NavigateIconSize)
                        .clip(RoundedCornerShape(8.dp)),
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surface,
        ),
        scrollBehavior = topAppBarScrollBehavior,
    )
}
