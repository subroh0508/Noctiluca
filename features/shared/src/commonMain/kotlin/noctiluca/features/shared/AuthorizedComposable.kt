package noctiluca.features.shared

import androidx.compose.runtime.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import noctiluca.features.navigation.backToSignIn
import noctiluca.features.navigation.navigateToTimelines
import noctiluca.features.shared.viewmodel.AuthorizedViewModel
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun AuthorizedComposable(
    vararg values: ProvidedValue<*>,
    eventStateFlow: AuthorizeEventStateFlow = rememberAuthorizeEventStateFlow(),
    content: @Composable () -> Unit,
) = FeatureComposable(*values) {
    val event by eventStateFlow.collectAsState(AuthorizedViewModel.Event.OK)

    when (event) {
        AuthorizedViewModel.Event.OK -> content()
        AuthorizedViewModel.Event.REOPEN -> {
            eventStateFlow.reset()
            navigateToTimelines()
        }

        AuthorizedViewModel.Event.SIGN_IN -> {
            eventStateFlow.reset()
            backToSignIn()
        }
    }
}

@Composable
fun rememberAuthorizeEventStateFlow() = remember {
    getKoin().get<AuthorizeEventStateFlow>()
}

class AuthorizeEventStateFlow(
    val state: MutableStateFlow<AuthorizedViewModel.Event>,
) : Flow<AuthorizedViewModel.Event> by state {
    constructor(
        initial: AuthorizedViewModel.Event = AuthorizedViewModel.Event.OK,
    ) : this(
        MutableStateFlow(initial),
    )

    fun reset() {
        state.value = AuthorizedViewModel.Event.OK
    }

    fun reopen() {
        state.value = AuthorizedViewModel.Event.REOPEN
    }

    fun requestSignIn() {
        state.value = AuthorizedViewModel.Event.SIGN_IN
    }
}
