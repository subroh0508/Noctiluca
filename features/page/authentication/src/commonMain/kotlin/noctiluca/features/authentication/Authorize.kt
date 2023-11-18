package noctiluca.features.authentication

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import noctiluca.features.authentication.model.AuthorizeResult
import noctiluca.features.authentication.viewmodel.AuthorizeViewModel
import noctiluca.features.navigation.navigateToTimelines
import noctiluca.features.shared.utils.openBrowser

@Composable
fun HandleAuthorize(
    authorizeResult: AuthorizeResult?,
    viewModel: AuthorizeViewModel,
) {
    LaunchedEffect(authorizeResult) {
        viewModel.fetchAccessToken(authorizeResult)
    }

    val event by viewModel.event.collectAsState()

    HandleAuthorizeEvent(event)
}

@Composable
private fun HandleAuthorizeEvent(
    event: AuthorizeViewModel.Event
) = when (event) {
    is AuthorizeViewModel.Event.OpeningBrowser -> openBrowser(event.uri)
    is AuthorizeViewModel.Event.NavigatingToTimelines -> navigateToTimelines()
    else -> Unit
}
