package noctiluca.features.shared

import androidx.compose.runtime.*
import noctiluca.features.navigation.navigateToSignIn
import noctiluca.features.navigation.navigateToTimelines
import noctiluca.features.shared.viewmodel.AuthorizedViewModel

@Composable
fun AuthorizedComposable(
    viewModel: AuthorizedViewModel,
    content: @Composable () -> Unit,
) {
    val event by viewModel.event.collectAsState()

    DisposableEffect(Unit) {
        onDispose { viewModel.reset() }
    }

    when (event) {
        AuthorizedViewModel.Event.OK -> content()
        AuthorizedViewModel.Event.REOPEN -> navigateToTimelines()
        AuthorizedViewModel.Event.SIGN_IN -> navigateToSignIn()
    }
}
