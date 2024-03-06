package noctiluca.features.timeline.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import noctiluca.features.shared.toot.FloatingTootCard
import noctiluca.features.timeline.TootScreen
import noctiluca.features.timeline.viewmodel.TootViewModel
import noctiluca.model.status.Status

@Composable
internal fun BoxScope.TootCard(
    viewModel: TootViewModel,
    modifier: Modifier = Modifier,
) {
    val navigator = LocalNavigator.current

    val expanded = remember { mutableStateOf(false) }

    val content = remember { mutableStateOf<String?>(null) }
    val warning = remember { mutableStateOf<String?>(null) }
    val visibility = remember { mutableStateOf(Status.Visibility.PUBLIC) }

    if (expanded.value) {
        FloatingTootCard(
            content,
            warning,
            visibility,
            expanded,
            onClickOpenFullScreen = { navigator?.push(TootScreen) },
            modifier = modifier,
        )

        return
    }

    FloatingActionButton(
        onClick = { expanded.value = true },
        modifier = Modifier.padding(16.dp)
            .align(Alignment.BottomEnd),
    ) {
        Icon(
            Icons.Default.Edit,
            contentDescription = "Expand Toot Card",
        )
    }
}
