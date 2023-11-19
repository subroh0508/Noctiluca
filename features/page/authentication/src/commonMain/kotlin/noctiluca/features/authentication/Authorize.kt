package noctiluca.features.authentication

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import noctiluca.features.authentication.model.AuthorizeResult
import noctiluca.features.authentication.model.buildRedirectUri
import noctiluca.features.authentication.viewmodel.AuthorizeViewModel
import noctiluca.features.navigation.navigateToTimelines
import noctiluca.features.shared.utils.openBrowser
import org.koin.core.parameter.parametersOf

@Composable
fun Screen.HandleAuthorize(
    domain: String,
    authorizeResult: AuthorizeResult?,
    content: @Composable (AuthorizeViewModel, Boolean) -> Unit,
) {
    val clientName = getString().sign_in_client_name
    val redirectUri = buildRedirectUri(domain)

    val viewModel: AuthorizeViewModel = getScreenModel { parametersOf(clientName, redirectUri) }

    LaunchedEffect(authorizeResult) {
        viewModel.fetchAccessToken(authorizeResult)
    }

    val event by viewModel.event.collectAsState()

    HandleDeepLink()
    HandleAuthorizeEvent(event)

    content(viewModel, viewModel.isFetchingAccessToken)
}

@Composable
private fun HandleAuthorizeEvent(
    event: AuthorizeViewModel.Event
) = when (event) {
    is AuthorizeViewModel.Event.OpeningBrowser -> openBrowser(event.uri)
    is AuthorizeViewModel.Event.NavigatingToTimelines -> navigateToTimelines()
    else -> Unit
}
