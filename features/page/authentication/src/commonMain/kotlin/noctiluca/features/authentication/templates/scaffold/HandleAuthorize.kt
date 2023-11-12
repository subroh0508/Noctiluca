package noctiluca.features.authentication.templates.scaffold

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.navigator.LocalNavigator
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import noctiluca.features.authentication.LocalAuthorizeResult
import noctiluca.features.authentication.viewmodel.AuthorizeViewModel
import noctiluca.features.components.utils.openBrowser

@Composable
fun HandleAuthorize(
    viewModel: AuthorizeViewModel,
) {
    val authorizeResult = LocalAuthorizeResult.current
    val navigator = LocalNavigator.current

    LaunchedEffect(authorizeResult) { viewModel.fetchAccessToken(authorizeResult) }

    val event by viewModel.event.subscribeAsState()

    when (event) {
        is AuthorizeViewModel.Event.OpeningBrowser -> openBrowser((event as AuthorizeViewModel.Event.OpeningBrowser).uri)
        is AuthorizeViewModel.Event.NavigatingToTimelines -> println("Sign In Success!") /* navigator.push() */
        else -> Unit
    }
}
