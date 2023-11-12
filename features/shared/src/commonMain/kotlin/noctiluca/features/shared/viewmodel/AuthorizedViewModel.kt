package noctiluca.features.shared.viewmodel

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import noctiluca.data.authentication.AuthorizedUserRepository
import noctiluca.features.components.ViewModel
import noctiluca.model.AuthorizedTokenNotFoundException
import noctiluca.model.HttpUnauthorizedException
import kotlin.coroutines.EmptyCoroutineContext

abstract class AuthorizedViewModel(
    private val authorizedUserRepository: AuthorizedUserRepository,
    coroutineScope: CoroutineScope,
) : ViewModel(coroutineScope) {
    enum class Event { OK, REOPEN, SIGN_IN }

    private val mutableEvent by lazy { MutableStateFlow(Event.OK) }
    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()

        when (e) {
            is HttpUnauthorizedException -> expireCurrentToken()
            is AuthorizedTokenNotFoundException -> requestSignIn()
        }
    }

    internal val event: StateFlow<Event> = mutableEvent

    protected fun launch(
        start: CoroutineStart,
        block: suspend CoroutineScope.() -> Unit
    ) = launch(
        exceptionHandler,
        start,
        block,
    )

    protected fun launchLazy(
        block: suspend CoroutineScope.() -> Unit,
    ) = launchLazy(
        exceptionHandler,
        block,
    )

    private fun expireCurrentToken() {
        launch(EmptyCoroutineContext) {
            runCatching { authorizedUserRepository.expireCurrent() }
                .onSuccess { reopen() }
                .onFailure { requestSignIn() }
        }
    }

    protected fun reopen() {
        mutableEvent.value = Event.REOPEN
    }

    protected fun requestSignIn() {
        mutableEvent.value = Event.SIGN_IN
    }
}
