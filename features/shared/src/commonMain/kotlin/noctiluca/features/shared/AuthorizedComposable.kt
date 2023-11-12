package noctiluca.features.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import noctiluca.features.navigation.navigateToSignIn
import noctiluca.features.navigation.navigateToTimelines
import noctiluca.features.shared.viewmodel.AuthorizedViewModel

@Composable
fun AuthorizedComposable(
    viewModel: AuthorizedViewModel,
    content: @Composable () -> Unit,
) {
    val event = viewModel.event.collectAsState()

    when (event.value) {
        AuthorizedViewModel.Event.OK -> content()
        AuthorizedViewModel.Event.REOPEN -> navigateToTimelines()
        AuthorizedViewModel.Event.SIGN_IN -> navigateToSignIn()
    }
}
