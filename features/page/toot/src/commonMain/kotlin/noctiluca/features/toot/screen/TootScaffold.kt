package noctiluca.features.toot.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import noctiluca.features.shared.extensions.getAuthorizedScreenModel
import noctiluca.features.shared.toot.TootBox
import noctiluca.features.toot.TootScreen
import noctiluca.features.toot.section.TootScrollableFrame
import noctiluca.features.toot.section.TootTopAppBar
import noctiluca.features.toot.viewmodel.TootViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TootScreen.TootScaffold() {
    val navigator = LocalNavigator.current

    val viewModel: TootViewModel = getAuthorizedScreenModel()
    val uiModel by viewModel.uiModel.collectAsState()

    LaunchedEffect(uiModel.isSendingSuccessful) {
        if (uiModel.isSendingSuccessful) {
            navigator?.pop()
        }
    }

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
            uiModel.statusText.content,
            uiModel.statusText.warning,
            enabled = !uiModel.message.isLoading,
            onChangeContent = viewModel::onChangeContent,
            onChangeWarningText = viewModel::onChangeWarningText,
            onClickToot = viewModel::toot,
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues),
        )
    }
}
