package noctiluca.features.shared.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.*
import noctiluca.data.authorization.AuthorizedUserRepository
import noctiluca.data.di.AuthorizedContext
import noctiluca.model.*
import org.koin.core.qualifier.TypeQualifier
import org.koin.core.scope.Scope
import org.koin.mp.KoinPlatform.getKoin

internal class AuthorizedContextImpl private constructor(
    private val repository: AuthorizedUserRepository,
    private val eventFlow: MutableStateFlow<AuthorizeEventState.Event>,
) : AuthorizedContext {
    constructor(
        repository: AuthorizedUserRepository,
        initial: AuthorizeEventState.Event = AuthorizeEventState.Event.OK,
    ) : this(
        repository,
        MutableStateFlow(initial),
    )

    override val state by lazy {
        combine(
            repository.currentAuthorizedUser(),
            eventFlow,
        ) { user, event -> AuthorizeEventState(user, event) }
            .onEach { (user, event) -> buildScope(user, event) }
            .catch { e ->
                when (e) {
                    is HttpUnauthorizedException -> emit(AuthorizeEventState(event = AuthorizeEventState.Event.REOPEN))
                    is AuthorizedTokenNotFoundException -> emit(AuthorizeEventState(event = AuthorizeEventState.Event.SIGN_IN))
                }
            }
    }

    override val scope get() = _scope

    override fun reset() {
        eventFlow.value = AuthorizeEventState.Event.OK
    }

    override fun reopen() {
        eventFlow.value = AuthorizeEventState.Event.REOPEN
    }

    override fun requestSignIn() {
        eventFlow.value = AuthorizeEventState.Event.SIGN_IN
    }

    override suspend fun switchCurrent(id: AccountId) {
        repository.switchCurrent(id)
        close()
    }

    override suspend fun expireCurrent() {
        repository.expireCurrent()
        close()
    }

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

        _scope = getKoin().createScope(scopeId, TypeQualifier(AuthorizedContext::class))
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
