package noctiluca.features.shared

import androidx.compose.runtime.*
import noctiluca.features.navigation.backToSignIn
import noctiluca.features.navigation.navigateToTimelines
import noctiluca.features.shared.viewmodel.AuthorizedViewModel

@Composable
fun AuthorizedComposable(
    viewModel: AuthorizedViewModel,
    content: @Composable () -> Unit,
) {
    val event by viewModel.event.collectAsState()

    when (event) {
        AuthorizedViewModel.Event.OK -> content()
        AuthorizedViewModel.Event.REOPEN -> {
            viewModel.reset()
            navigateToTimelines()
        }

        AuthorizedViewModel.Event.SIGN_IN -> {
            viewModel.reset()
            backToSignIn()
        }
    }
}
