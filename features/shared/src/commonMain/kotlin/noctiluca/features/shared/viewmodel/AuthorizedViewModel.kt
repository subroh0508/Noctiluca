package noctiluca.features.shared.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import noctiluca.data.authentication.AuthorizedUserRepository
import noctiluca.model.AuthorizedTokenNotFoundException
import noctiluca.model.HttpUnauthorizedException
import kotlin.coroutines.EmptyCoroutineContext

abstract class AuthorizedViewModel(
    protected val authorizedUserRepository: AuthorizedUserRepository,
    coroutineScope: CoroutineScope,
) : ViewModel(coroutineScope) {
    enum class Event { OK, REOPEN, SIGN_IN }

    private val mutableEvent by lazy { MutableStateFlow(Event.OK) }

    internal val event: StateFlow<Event> = mutableEvent

    protected inline fun <R> runCatchingWithAuth(
        block: () -> R,
    ) = runCatching(block).apply {
        exceptionOrNull()?.let { handleException(it) }
    }

    protected fun handleException(e: Throwable) {
        e.printStackTrace()

        when (e) {
            is HttpUnauthorizedException -> expireCurrentToken()
            is AuthorizedTokenNotFoundException -> requestSignIn()
        }
    }

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
