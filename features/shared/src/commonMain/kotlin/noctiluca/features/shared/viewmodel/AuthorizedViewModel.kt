package noctiluca.features.shared.viewmodel

import kotlinx.coroutines.flow.*
import noctiluca.data.authentication.AuthorizedUserRepository
import noctiluca.model.AuthorizedTokenNotFoundException
import noctiluca.model.HttpUnauthorizedException
import kotlin.coroutines.EmptyCoroutineContext

abstract class AuthorizedViewModel(
    protected val authorizedUserRepository: AuthorizedUserRepository,
) : ViewModel() {
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

    internal fun reset() {
        mutableEvent.value = Event.OK
    }

    protected fun reopen() {
        mutableEvent.value = Event.REOPEN
    }

    protected fun requestSignIn() {
        mutableEvent.value = Event.SIGN_IN
    }

    protected fun <T1, T2, T3, T4, T5, R> buildUiModel(
        flow: Flow<T1>,
        flow2: Flow<T2>,
        flow3: Flow<T3>,
        flow4: Flow<T4>,
        flow5: Flow<T5>,
        initialValue: R,
        started: SharingStarted = SharingStarted.WhileSubscribed(),
        transform: suspend (T1, T2, T3, T4, T5) -> R,
    ) = combine(
        flow,
        flow2,
        flow3,
        flow4,
        flow5,
        transform,
    ).catch { e ->
        handleException(e)
    }.stateIn(
        scope = viewModelScope,
        started = started,
        initialValue = initialValue,
    )
}
