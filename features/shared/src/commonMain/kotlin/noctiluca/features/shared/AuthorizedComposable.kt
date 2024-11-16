package noctiluca.features.shared

import androidx.compose.runtime.*
import kotlinx.coroutines.flow.MutableStateFlow
import noctiluca.data.di.AuthorizedContext
import noctiluca.features.navigation.backToSignIn
import noctiluca.features.navigation.navigateToTimelines
import noctiluca.features.shared.viewmodel.rememberAuthorizedContext
import noctiluca.model.AccountId
import noctiluca.model.AuthorizeEventState

val LocalAuthorizedContext = compositionLocalOf<AuthorizedContext> { EmptyAuthorizedContext }

@Composable
fun AuthorizedComposable(
    vararg values: ProvidedValue<*>,
    context: AuthorizedContext = rememberAuthorizedContext(),
    content: @Composable () -> Unit,
) = FeatureComposable(
    LocalAuthorizedContext provides context,
    *values,
) {
    val state by context.state.collectAsState(AuthorizeEventState())

    when (state.event) {
        AuthorizeEventState.Event.OK -> AuthorizedContextContent(
            context.scope != null,
            content,
        )
        AuthorizeEventState.Event.REOPEN -> {
            context.reset()
            navigateToTimelines(state.user)
        }

        AuthorizeEventState.Event.SIGN_IN -> {
            context.reset()
            backToSignIn()
        }
    }
}

@Composable
private fun AuthorizedContextContent(
    hasScope: Boolean,
    content: @Composable () -> Unit,
) {
    if (!hasScope) {
        return
    }

    content()
}

private data object EmptyAuthorizedContext : AuthorizedContext {
    override val state = MutableStateFlow(AuthorizeEventState())
    override val scope = null
    override fun reset() = Unit
    override fun reopen() = Unit
    override fun requestSignIn() = Unit
    override suspend fun switchCurrent(id: AccountId) = Unit
    override suspend fun expireCurrent() = Unit
}
