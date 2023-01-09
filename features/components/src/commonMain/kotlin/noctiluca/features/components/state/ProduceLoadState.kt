package noctiluca.features.components.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProduceStateScope
import androidx.compose.runtime.produceState
import kotlinx.coroutines.CoroutineScope
import noctiluca.features.components.model.LoadState
import noctiluca.features.components.utils.loadLazy
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

fun <T: Any> ProduceLoadStateScope.loadLazy(
    block: suspend CoroutineScope.() -> T,
) = loadLazy(this, block)
