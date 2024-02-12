package noctiluca.features.shared

import androidx.compose.runtime.*
import noctiluca.data.di.AuthorizedContext
import noctiluca.features.navigation.backToSignIn
import noctiluca.features.navigation.navigateToTimelines
import noctiluca.features.shared.viewmodel.AuthorizedContextViewModel
import noctiluca.features.shared.viewmodel.rememberAuthorizedContext
import noctiluca.model.AuthorizeEventState

@Composable
fun AuthorizedComposable(
    vararg values: ProvidedValue<*>,
    context: AuthorizedContext = rememberAuthorizedContext(),
    content: @Composable (AuthorizedContext) -> Unit,
) = FeatureComposable(*values) {
    val state by context.state.collectAsState()

    when (state.event) {
        AuthorizeEventState.Event.OK -> AuthorizedContextContent(context, content)
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

@Composable
private fun AuthorizedContextContent(
    context: AuthorizedContext,
    content: @Composable (AuthorizedContext) -> Unit,
) {
    context.scope ?: return

    content(context)
}
