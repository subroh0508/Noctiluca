package noctiluca.features.toot.section

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noctiluca.features.navigation.component.Back
import noctiluca.features.shared.toot.VisibilityChip
import noctiluca.model.status.Status

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TootTopAppBar(
    visibility: Status.Visibility,
    onChangeVisibility: (Status.Visibility) -> Unit,
) = TopAppBar(
    title = {},
    navigationIcon = { Back() },
    actions = {
        VisibilityChip(
            visibility,
            onChangeVisibility = onChangeVisibility,
        )
        Spacer(Modifier.width(12.dp))
    },
    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.surface,
        scrolledContainerColor = MaterialTheme.colorScheme.surface,
    ),
    scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
)
