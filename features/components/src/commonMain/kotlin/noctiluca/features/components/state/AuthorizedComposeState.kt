package noctiluca.features.components.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import noctiluca.features.components.LocalCoroutineExceptionHandler
import noctiluca.features.components.UnauthorizedExceptionHandler

interface AuthorizedComposeState {
    val exceptionHandler: UnauthorizedExceptionHandler

    companion object {
        operator fun invoke(
            handler: UnauthorizedExceptionHandler = UnauthorizedExceptionHandler(),
        ): AuthorizedComposeState = Impl(handler)
    }

    private class Impl(
        override val exceptionHandler: UnauthorizedExceptionHandler,
    ) : AuthorizedComposeState
}

@Composable
fun rememberAuthorizedComposeState(): AuthorizedComposeState {
    val handler = LocalCoroutineExceptionHandler.current

    return remember { AuthorizedComposeState(handler) }
}

inline fun <R> AuthorizedComposeState.runCatchingWithAuth(
    block: AuthorizedComposeState.() -> R,
) = runCatching(block).apply {
    exceptionOrNull()?.let(exceptionHandler::handleException)
}
