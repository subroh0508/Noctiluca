package noctiluca.components.state

import androidx.compose.runtime.Composable
import kotlinx.coroutines.*
import noctiluca.components.LocalCoroutineExceptionHandler
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

interface AuthorizedComposeState<T> {
    companion object {
        operator fun <T> invoke(
            handler: CoroutineExceptionHandler,
        ): AuthorizedComposeState<T> = Impl(handler)

        @Composable
        operator fun <T> invoke() = invoke<T>(LocalCoroutineExceptionHandler.current)
    }

    fun CoroutineScope.launchWithAuth(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ): Job

    fun <E> CoroutineScope.asyncWithAuth(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> E
    ): Deferred<E>

    private class Impl<T>(
        private val coroutineExceptionHandler: CoroutineExceptionHandler,
    ) : AuthorizedComposeState<T> {
        override fun CoroutineScope.launchWithAuth(
            context: CoroutineContext,
            start: CoroutineStart,
            block: suspend CoroutineScope.() -> Unit
        ) = launch(context + coroutineExceptionHandler, start, block)

        override fun <E> CoroutineScope.asyncWithAuth(
            context: CoroutineContext,
            start: CoroutineStart,
            block: suspend CoroutineScope.() -> E
        ) = async(context + coroutineExceptionHandler, start, block)
    }
}