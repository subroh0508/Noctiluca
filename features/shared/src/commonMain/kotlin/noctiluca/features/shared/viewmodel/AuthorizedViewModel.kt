package noctiluca.features.shared.viewmodel

import kotlinx.coroutines.flow.*
import noctiluca.data.di.AuthorizedContext
import noctiluca.features.shared.model.LoadState
import noctiluca.model.AuthorizedTokenNotFoundException
import noctiluca.model.HttpUnauthorizedException
import kotlin.coroutines.EmptyCoroutineContext

abstract class AuthorizedViewModel(
    protected val context: AuthorizedContext,
) : ViewModel() {
    protected inline fun launchWithAuth(
        state: MutableStateFlow<LoadState>,
        crossinline block: suspend () -> Unit,
    ) {
        val job = launch {
            runCatchingWithAuth { block() }
                .onSuccess { state.value = LoadState.Loaded }
                .onFailure { e -> state.value = LoadState.Error(e) }
        }

        state.value = LoadState.Loading(job)
    }

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
            runCatching { context.expireCurrent() }
                .onSuccess { reopen() }
                .onFailure { requestSignIn() }
        }
    }

    internal fun reset() = context.reset()

    protected fun reopen() = context.reopen()

    protected fun requestSignIn() = context.requestSignIn()

    protected fun <T1, T2, R> buildUiModel(
        flow: Flow<T1>,
        flow2: Flow<T2>,
        initialValue: R,
        started: SharingStarted = SharingStarted.WhileSubscribed(),
        catch: suspend (Throwable) -> Unit = {},
        transform: suspend (T1, T2) -> R,
    ) = combine(
        flow,
        flow2,
        transform,
    ).stateInWithAuth(
        started = started,
        catch = catch,
        initialValue = initialValue,
    )

    protected fun <T1, T2, T3, R> buildUiModel(
        flow: Flow<T1>,
        flow2: Flow<T2>,
        flow3: Flow<T3>,
        initialValue: R,
        started: SharingStarted = SharingStarted.WhileSubscribed(),
        catch: suspend (Throwable) -> Unit = {},
        transform: suspend (T1, T2, T3) -> R,
    ) = combine(
        flow,
        flow2,
        flow3,
        transform,
    ).stateInWithAuth(
        started = started,
        catch = catch,
        initialValue = initialValue,
    )

    protected fun <T1, T2, T3, T4, R> buildUiModel(
        flow: Flow<T1>,
        flow2: Flow<T2>,
        flow3: Flow<T3>,
        flow4: Flow<T4>,
        initialValue: R,
        started: SharingStarted = SharingStarted.WhileSubscribed(),
        catch: suspend (Throwable) -> Unit = {},
        transform: suspend (T1, T2, T3, T4) -> R,
    ) = combine(
        flow,
        flow2,
        flow3,
        flow4,
        transform,
    ).stateInWithAuth(
        started = started,
        catch = catch,
        initialValue = initialValue,
    )

    protected fun <T1, T2, T3, T4, T5, R> buildUiModel(
        flow: Flow<T1>,
        flow2: Flow<T2>,
        flow3: Flow<T3>,
        flow4: Flow<T4>,
        flow5: Flow<T5>,
        initialValue: R,
        started: SharingStarted = SharingStarted.WhileSubscribed(),
        catch: suspend (Throwable) -> Unit = {},
        transform: suspend (T1, T2, T3, T4, T5) -> R,
    ) = combine(
        flow,
        flow2,
        flow3,
        flow4,
        flow5,
        transform,
    ).stateInWithAuth(
        started = started,
        catch = catch,
        initialValue = initialValue,
    )

    private fun <R> Flow<R>.stateInWithAuth(
        started: SharingStarted,
        catch: suspend (Throwable) -> Unit,
        initialValue: R,
    ) = catch { e ->
        handleException(e)
        catch(e)
    }.stateIn(
        scope = viewModelScope,
        started = started,
        initialValue = initialValue,
    )
}
