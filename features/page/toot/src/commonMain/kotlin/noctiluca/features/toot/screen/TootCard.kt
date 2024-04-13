package noctiluca.features.toot.screen

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
import noctiluca.features.toot.TootScreen
import noctiluca.features.toot.viewmodel.TootViewModel

@Composable
fun BoxScope.TootCard(
    viewModel: TootViewModel,
    modifier: Modifier = Modifier,
) {
    val navigator = LocalNavigator.current

    val uiModel by viewModel.uiModel.collectAsState()
    val expanded = remember { mutableStateOf(false) }

    val content = remember { mutableStateOf<String?>(null) }
    val warning = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(uiModel.isSendingSuccessful) {
        if (uiModel.isSendingSuccessful) {
            content.value = uiModel.statusText.content
            warning.value = uiModel.statusText.warning
        }
    }

    viewModel.onChange(content.value, warning.value)

    if (expanded.value) {
        FloatingTootCard(
            content,
            warning,
            uiModel.statusText.visibility,
            enabled = !uiModel.message.isLoading,
            expanded = expanded,
            onChangeVisibility = viewModel::onChangeVisibility,
            onClickOpenFullScreen = { navigator?.push(TootScreen) },
            onClickToot = viewModel::toot,
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
