package noctiluca.features.toot.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import noctiluca.features.shared.extensions.getAuthorizedScreenModel
import noctiluca.features.shared.toot.TootBox
import noctiluca.features.toot.TootScreen
import noctiluca.features.toot.section.TootTopAppBar
import noctiluca.features.toot.viewmodel.TootViewModel
import noctiluca.model.status.Status

@Composable
internal fun TootScreen.TootScaffold() {
    val viewModel: TootViewModel = getAuthorizedScreenModel()
    val uiModel by viewModel.uiModel.collectAsState()

    val content = remember { mutableStateOf<String?>(null) }
    val warning = remember { mutableStateOf<String?>(null) }
    val visibility = remember { mutableStateOf(Status.Visibility.PUBLIC) }

    Scaffold(
        topBar = { TootTopAppBar(visibility) },
    ) { paddingValues ->
        TootBox(
            uiModel.current,
            content,
            warning,
            modifier = Modifier.fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding()),
        )
    }
}
