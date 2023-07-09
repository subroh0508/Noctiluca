package noctiluca.features.timeline.template.scaffold

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import noctiluca.features.shared.toot.TootBox
import noctiluca.features.timeline.LocalNavigation
import noctiluca.features.timeline.state.rememberCurrentAuthorizedAccountStatus
import noctiluca.features.timeline.template.scaffold.toot.TootTopAppBar
import noctiluca.features.timeline.viewmodel.TimelinesViewModel
import noctiluca.status.model.Status

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TootScaffold(
    viewModel: TimelinesViewModel,
) {
    LaunchedEffect(Unit) {
        viewModel.loadCurrentAuthorizedAccount()
    }

    val uiModel by viewModel.uiModel.subscribeAsState()

    val navigation = LocalNavigation.current

    val content = remember { mutableStateOf<String?>(null) }
    val warning = remember { mutableStateOf<String?>(null) }
    val visibility = remember { mutableStateOf(Status.Visibility.PUBLIC) }

    Scaffold(
        topBar = {
            TootTopAppBar(
                visibility,
                onBackPressed = { navigation?.backPressed() },
            )
        },
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
