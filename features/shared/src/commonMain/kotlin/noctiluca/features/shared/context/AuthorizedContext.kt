package noctiluca.features.shared.context

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.*
import noctiluca.data.authorization.AuthorizedUserRepository
import noctiluca.features.shared.model.AuthorizeEventState
import noctiluca.features.shared.viewmodel.ViewModel
import noctiluca.features.shared.viewmodel.viewModelScope
import noctiluca.model.AuthorizedTokenNotFoundException
import noctiluca.model.AuthorizedUser
import noctiluca.model.HttpUnauthorizedException
import org.koin.core.component.getScopeName
import org.koin.core.scope.Scope
import org.koin.mp.KoinPlatform.getKoin

class AuthorizedContext private constructor(
    private val repository: AuthorizedUserRepository,
    private val eventFlow: MutableStateFlow<AuthorizeEventState.Event>,
) : ViewModel() {
    constructor(
        repository: AuthorizedUserRepository,
        initial: AuthorizeEventState.Event = AuthorizeEventState.Event.OK,
    ) : this(
        repository,
        MutableStateFlow(initial),
    )

    val state by lazy {
        combine(
            repository.currentAuthorizedUser(),
            eventFlow,
        ) { user, event -> AuthorizeEventState(user, event) }
            .onEach { (user, event) -> buildScope(user, event) }
            .catch { e ->
                when (e) {
                    is HttpUnauthorizedException -> emit(AuthorizeEventState(event = AuthorizeEventState.Event.REOPEN))
                    is AuthorizedTokenNotFoundException -> emit(AuthorizeEventState(event = AuthorizeEventState.Event.SIGN_IN))
                    else -> throw e
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = AuthorizeEventState(),
            )
    }

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

    val scope get() = _scope

    private var _scope: Scope? = null
    private fun buildScope(
        user: AuthorizedUser?,
        event: AuthorizeEventState.Event,
    ) {
        if (user != null && event == AuthorizeEventState.Event.OK) {
            create(user)
            return
        }

        close()
    }

    private fun create(user: AuthorizedUser) {
        val scope = _scope
        val scopeId = "${user.id.value}@${user.domain.value}"
        if (scope != null && !scope.closed && scope.id == scopeId) {
            return
        }

        _scope = getKoin().createScope(scopeId, getScopeName())
    }

    private fun close() {
        _scope?.close()
        _scope = null
    }
}

@Composable
fun rememberAuthorizedContext() = remember {
    getKoin().get<AuthorizedContext>()
}
