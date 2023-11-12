package noctiluca.features.authentication.templates.scaffold

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import noctiluca.features.authentication.model.AuthorizeResult
import noctiluca.features.authentication.viewmodel.AuthorizeViewModel
import noctiluca.features.components.utils.openBrowser
import noctiluca.features.navigation.navigateToTimelines

@Composable
fun HandleAuthorize(
    authorizeResult: AuthorizeResult?,
    viewModel: AuthorizeViewModel,
) {
    LaunchedEffect(authorizeResult) {
        viewModel.fetchAccessToken(authorizeResult)
    }

    val event by viewModel.event.subscribeAsState()

    HandleEvent(event)
}

@Composable
private fun HandleEvent(
    event: AuthorizeViewModel.Event
) = when (event) {
    is AuthorizeViewModel.Event.OpeningBrowser -> openBrowser(event.uri)
    is AuthorizeViewModel.Event.NavigatingToTimelines -> navigateToTimelines()
    else -> Unit
}
