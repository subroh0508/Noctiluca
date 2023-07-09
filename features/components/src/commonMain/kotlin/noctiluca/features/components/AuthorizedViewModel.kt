package noctiluca.features.components

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import kotlinx.coroutines.CoroutineScope

abstract class AuthorizedViewModel(
    coroutineScope: CoroutineScope,
    lifecycleRegistry: LifecycleRegistry,
    componentContext: ComponentContext,
    val exceptionHandler: UnauthorizedExceptionHandler,
) : ViewModel(coroutineScope) {
    inline fun <R> runCatchingWithAuth(
        block: () -> R,
    ) = runCatching(block).apply {
        exceptionOrNull()?.let(exceptionHandler::handleException)
    }
}
