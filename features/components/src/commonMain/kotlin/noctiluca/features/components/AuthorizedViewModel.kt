package noctiluca.features.components

import kotlinx.coroutines.CoroutineScope

abstract class AuthorizedViewModel(
    coroutineScope: CoroutineScope,
    val exceptionHandler: UnauthorizedExceptionHandler,
) : ViewModel(coroutineScope) {
    inline fun <R> runCatchingWithAuth(
        block: () -> R,
    ) = runCatching(block).apply {
        exceptionOrNull()?.let(exceptionHandler::handleException)
    }
}
