package noctiluca.components.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProduceStateScope
import androidx.compose.runtime.remember
import kotlinx.coroutines.*
import noctiluca.components.LocalCoroutineExceptionHandler
import noctiluca.components.UnauthorizedExceptionHandler
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

interface AuthorizedComposeState<T> {
    val exceptionHandler: UnauthorizedExceptionHandler

    companion object {
        operator fun <T> invoke(
            handler: UnauthorizedExceptionHandler = UnauthorizedExceptionHandler(),
        ): AuthorizedComposeState<T> = Impl(handler)
    }

    private class Impl<T>(
        override val exceptionHandler: UnauthorizedExceptionHandler,
    ) : AuthorizedComposeState<T>
}

@Composable
fun <T> rememberAuthorizedComposeState(): AuthorizedComposeState<T> {
    val handler = LocalCoroutineExceptionHandler.current

    return remember { AuthorizedComposeState(handler) }
}

inline fun <T, R> AuthorizedComposeState<T>.runCatchingWithAuth(
    block: AuthorizedComposeState<T>.() -> R,
) = runCatching(block).apply {
    exceptionOrNull()?.let(exceptionHandler::handleException)
}
