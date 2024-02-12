package noctiluca.features.shared

import androidx.compose.runtime.*
import noctiluca.features.navigation.backToSignIn
import noctiluca.features.navigation.navigateToTimelines
import noctiluca.features.shared.context.AuthorizedContext
import noctiluca.features.shared.context.rememberAuthorizedContext
import noctiluca.features.shared.model.AuthorizeEventState

@Composable
fun AuthorizedComposable(
    vararg values: ProvidedValue<*>,
    context: AuthorizedContext = rememberAuthorizedContext(),
    content: @Composable () -> Unit,
) = FeatureComposable(*values) {
    val state by context.state.collectAsState()

    when (state.event) {
        AuthorizeEventState.Event.OK -> content()
        AuthorizeEventState.Event.REOPEN -> {
            context.reset()
            navigateToTimelines()
        }

        AuthorizeEventState.Event.SIGN_IN -> {
            context.reset()
            backToSignIn()
        }
    }
}
