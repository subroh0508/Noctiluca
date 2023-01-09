package noctiluca.features.components.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProduceStateScope
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import kotlinx.coroutines.*
import noctiluca.features.components.LocalCoroutineExceptionHandler
import noctiluca.features.components.UnauthorizedExceptionHandler
import noctiluca.features.components.model.LoadState
import kotlin.experimental.ExperimentalTypeInference

@OptIn(ExperimentalTypeInference::class)
@Composable
fun <T> produceAuthorizedState(
    initialValue: T,
    vararg keys: Any?,
    @BuilderInference producer: ProduceAuthorizedStateScope<T>.() -> Unit,
): State<T> {
    val handler = LocalCoroutineExceptionHandler.current

    return produceState(
        initialValue,
        keys = keys,
    ) { ProduceAuthorizedStateScope(this, handler).producer() }
}

typealias ProduceAuthorizedLoadStateScope = ProduceAuthorizedStateScope<LoadState>

@OptIn(ExperimentalTypeInference::class)
@Composable
fun produceAuthorizedLoadState(
    vararg keys: Any?,
    @BuilderInference producer: ProduceAuthorizedStateScope<LoadState>.() -> Unit,
) = produceAuthorizedState(
    initialValue = LoadState.Initial,
    keys = keys,
    producer = producer,
)

fun <T: Any> ProduceAuthorizedLoadStateScope.loadLazy(
    block: suspend CoroutineScope.() -> T,
) {
    val job = launch(start = CoroutineStart.LAZY) {
        runCatchingWithAuth { block() }
            .onSuccess { value = LoadState.Loaded(it) }
            .onFailure { value = LoadState.Error(it) }
    }

    value = LoadState.Loading(job)
    job.start()
}

class ProduceAuthorizedStateScope<T>(
    scope: ProduceStateScope<T>,
    val exceptionHandler: UnauthorizedExceptionHandler,
) : ProduceStateScope<T> by scope {
    inline fun <R> runCatchingWithAuth(
        block: ProduceStateScope<T>.() -> R,
    ) = runCatching(block).apply {
        exceptionOrNull()?.let(exceptionHandler::handleException)
    }
}
