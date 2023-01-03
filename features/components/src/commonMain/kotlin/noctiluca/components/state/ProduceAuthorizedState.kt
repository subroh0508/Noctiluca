package noctiluca.components.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProduceStateScope
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import kotlinx.coroutines.*
import noctiluca.components.LocalCoroutineExceptionHandler
import noctiluca.components.model.LoadState
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
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
    val job = launchWithAuth(start = CoroutineStart.LAZY) {
        runCatching { block() }
            .onSuccess { value = LoadState.Loaded(it) }
            .onFailure { value = LoadState.Error(it) }
    }

    value = LoadState.Loading(job)
    job.start()
}

class ProduceAuthorizedStateScope<T>(
    scope: ProduceStateScope<T>,
    private val coroutineExceptionHandler: CoroutineExceptionHandler,
) : ProduceStateScope<T> by scope {
    fun CoroutineScope.launchWithAuth(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    )= launch(context + coroutineExceptionHandler, start, block)

    fun <E> CoroutineScope.asyncWithAuth(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> E
    ) = async(context + coroutineExceptionHandler, start, block)
}
