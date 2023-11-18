package noctiluca.features.timeline.template.scaffold.toot

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noctiluca.features.shared.atoms.appbar.TopAppBar
import noctiluca.features.shared.toot.internal.VisibilityChip
import noctiluca.model.status.Status

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TootTopAppBar(
    visibility: MutableState<Status.Visibility>,
    onBackPressed: () -> Unit,
) = TopAppBar(
    navigationIcon = {
        IconButton(onClick = onBackPressed) {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = "Back",
            )
        }
    },
    actions = {
        VisibilityChip(visibility)
        Spacer(Modifier.width(12.dp))
    },
    scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
)
