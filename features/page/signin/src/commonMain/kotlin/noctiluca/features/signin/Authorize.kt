package noctiluca.features.signin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import noctiluca.features.navigation.navigateToTimelines
import noctiluca.features.navigation.utils.getFeaturesScreenModel
import noctiluca.features.shared.atoms.snackbar.showSnackbar
import noctiluca.features.shared.getCommonString
import noctiluca.features.shared.utils.openBrowser
import noctiluca.features.signin.model.AuthorizeResult
import noctiluca.features.signin.model.buildRedirectUri
import noctiluca.features.signin.viewmodel.AuthorizeViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun SignInScreen.HandleAuthorize(
    domain: String,
    authorizeResult: AuthorizeResult?,
    content: @Composable (AuthorizeViewModel, Boolean) -> Unit,
) {
    val clientName = getString().sign_in_client_name
    val redirectUri = buildRedirectUri(domain)

    val viewModel: AuthorizeViewModel = getFeaturesScreenModel {
        parametersOf(clientName, redirectUri)
    }

    LaunchedEffect(authorizeResult) {
        viewModel.fetchAccessToken(authorizeResult)
    }

    val event by viewModel.event.collectAsState()

    HandleDeepLink()
    HandleAuthorizeEvent(event)

    SnackbarForAuthorizationError(authorizeResult)

    content(
        viewModel,
        authorizeResult?.getCodeOrNull() != null && viewModel.isFetchingAccessToken,
    )
}

@Composable
private fun HandleAuthorizeEvent(
    event: AuthorizeViewModel.Event
) = when (event) {
    is AuthorizeViewModel.Event.OpeningBrowser -> openBrowser(event.uri)
    is AuthorizeViewModel.Event.NavigatingToTimelines -> navigateToTimelines()
    else -> Unit
}

@Composable
private fun SnackbarForAuthorizationError(
    authorizeResult: AuthorizeResult?,
) {
    val error = authorizeResult?.getErrorOrNull() ?: return

    showSnackbar(error.message ?: getCommonString().error_unknown)
}
