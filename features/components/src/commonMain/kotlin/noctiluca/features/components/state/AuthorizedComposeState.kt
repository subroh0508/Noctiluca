package noctiluca.features.components.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import noctiluca.features.components.LocalCoroutineExceptionHandler
import noctiluca.features.components.UnauthorizedExceptionHandler

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
