package noctiluca.components.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProduceStateScope
import kotlinx.coroutines.*
import noctiluca.components.LocalCoroutineExceptionHandler
import noctiluca.components.UnauthorizedExceptionHandler
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

interface AuthorizedComposeState<T> {
    val exceptionHandler: UnauthorizedExceptionHandler

    companion object {
        operator fun <T> invoke(
            handler: UnauthorizedExceptionHandler,
        ): AuthorizedComposeState<T> = Impl(handler)

        @Composable
        operator fun <T> invoke() = invoke<T>(LocalCoroutineExceptionHandler.current)
    }

    private class Impl<T>(
        override val exceptionHandler: UnauthorizedExceptionHandler,
    ) : AuthorizedComposeState<T>
}

inline fun <T, R> AuthorizedComposeState<T>.runCatchingWithAuth(
    block: AuthorizedComposeState<T>.() -> R,
) = runCatching(block).apply {
    exceptionOrNull()?.let(exceptionHandler::handleException)
}
