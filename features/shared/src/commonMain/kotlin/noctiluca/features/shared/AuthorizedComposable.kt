package noctiluca.features.shared

import androidx.compose.runtime.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import noctiluca.data.authorization.AuthorizedUserRepository
import noctiluca.features.navigation.backToSignIn
import noctiluca.features.navigation.navigateToTimelines
import noctiluca.features.shared.model.AuthorizeEventState
import noctiluca.model.AuthorizedTokenNotFoundException
import noctiluca.model.HttpUnauthorizedException
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun AuthorizedComposable(
    vararg values: ProvidedValue<*>,
    eventStateFlow: AuthorizeEventStateFlow = rememberAuthorizeEventStateFlow(),
    content: @Composable () -> Unit,
) = FeatureComposable(*values) {
    val state by eventStateFlow.catch { e ->
        e.printStackTrace()

        when (e) {
            is HttpUnauthorizedException -> eventStateFlow.reopen()
            is AuthorizedTokenNotFoundException -> eventStateFlow.requestSignIn()
        }
    }.collectAsState(AuthorizeEventState())

    when (state.event) {
        AuthorizeEventState.Event.OK -> content()
        AuthorizeEventState.Event.REOPEN -> {
            eventStateFlow.reset()
            navigateToTimelines()
        }

        AuthorizeEventState.Event.SIGN_IN -> {
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
    private val repository: AuthorizedUserRepository,
    private val eventFlow: MutableStateFlow<AuthorizeEventState.Event>,
) : Flow<AuthorizeEventState> by combine(
    repository.currentAuthorizedUser(),
    eventFlow,
    transform = { user, event -> AuthorizeEventState(user, event) },
) {
    constructor(
        repository: AuthorizedUserRepository,
        initial: AuthorizeEventState.Event = AuthorizeEventState.Event.OK,
    ) : this(
        repository,
        MutableStateFlow(initial),
    )

    fun reset() {
        eventFlow.value = AuthorizeEventState.Event.OK
    }

    fun reopen() {
        eventFlow.value = AuthorizeEventState.Event.REOPEN
    }

    fun requestSignIn() {
        eventFlow.value = AuthorizeEventState.Event.SIGN_IN
    }

    suspend fun expireCurrent() = repository.expireCurrent()
}
