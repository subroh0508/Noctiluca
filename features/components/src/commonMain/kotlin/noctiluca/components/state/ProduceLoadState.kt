package noctiluca.components.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProduceStateScope
import androidx.compose.runtime.produceState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import noctiluca.components.model.LoadState
import kotlin.experimental.ExperimentalTypeInference

typealias ProduceLoadStateScope = ProduceStateScope<LoadState>

@OptIn(ExperimentalTypeInference::class)
@Composable
fun produceLoadState(
    vararg keys: Any?,
    @BuilderInference producer: suspend ProduceLoadStateScope.() -> Unit
) = produceState(
    initialValue = LoadState.Initial,
    keys = keys,
    producer = producer,
)

fun ProduceLoadStateScope.loadLazy(
    block: suspend CoroutineScope.() -> Unit,
) {
    val job = launch(start = CoroutineStart.LAZY) {
        runCatching { block() }
            .onSuccess { value = LoadState.Loaded(it) }
            .onFailure { value = LoadState.Error(it) }
    }

    value = LoadState.Loading(job)
    job.start()
}
