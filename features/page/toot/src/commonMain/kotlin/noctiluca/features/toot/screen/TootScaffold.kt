package noctiluca.features.toot.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import noctiluca.features.shared.extensions.getAuthorizedScreenModel
import noctiluca.features.toot.TootScreen
import noctiluca.features.toot.section.TootBox
import noctiluca.features.toot.section.TootScrollableFrame
import noctiluca.features.toot.section.TootTopAppBar
import noctiluca.features.toot.viewmodel.TootViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TootScreen.TootScaffold() {
    val navigator = LocalNavigator.current

    val viewModel: TootViewModel = getAuthorizedScreenModel()
    val uiModel by viewModel.uiModel.collectAsState()

    val content = remember { mutableStateOf<String?>(null) }
    val warning = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(uiModel.isSendingSuccessful) {
        if (uiModel.isSendingSuccessful) {
            content.value = uiModel.statusText.content
            warning.value = uiModel.statusText.warning
            navigator?.pop()
        }
    }

    viewModel.onChange(content.value, warning.value)

    TootScrollableFrame(
        uiModel.message,
        topBar = { scrollBehavior ->
            TootTopAppBar(
                uiModel.statusText.visibility,
                enabled = !uiModel.message.isLoading,
                onChangeVisibility = viewModel::onChangeVisibility,
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        TootBox(
            uiModel.current,
            content,
            warning,
            enabled = !uiModel.message.isLoading,
            onClickToot = viewModel::toot,
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues),
        )
    }
}
