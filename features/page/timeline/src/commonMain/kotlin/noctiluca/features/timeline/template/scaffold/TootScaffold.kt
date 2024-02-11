package noctiluca.features.timeline.template.scaffold

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import noctiluca.features.shared.toot.TootBox
import noctiluca.features.timeline.template.scaffold.toot.TootTopAppBar
import noctiluca.features.timeline.viewmodel.TimelinesViewModel
import noctiluca.model.status.Status

@Composable
internal fun TootScaffold(
    viewModel: TimelinesViewModel,
) {
    val uiModel by viewModel.uiModel.collectAsState()

    val content = remember { mutableStateOf<String?>(null) }
    val warning = remember { mutableStateOf<String?>(null) }
    val visibility = remember { mutableStateOf(Status.Visibility.PUBLIC) }

    Scaffold(
        topBar = { TootTopAppBar(visibility) },
    ) { paddingValues ->
        TootBox(
            uiModel.account.current,
            content,
            warning,
            modifier = Modifier.fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding()),
        )
    }
}
