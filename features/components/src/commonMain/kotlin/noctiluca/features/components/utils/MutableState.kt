package noctiluca.features.components.utils

import androidx.compose.runtime.MutableState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import noctiluca.features.components.model.LoadState

internal fun <T: Any> CoroutineScope.loadLazy(
    state: MutableState<LoadState>,
    block: suspend CoroutineScope.() -> T,
) {
    val job = launch(start = CoroutineStart.LAZY) {
        runCatching { block() }
            .onSuccess { state.value = LoadState.Loaded(it) }
            .onFailure { it.printStackTrace(); state.value = LoadState.Error(it) }
    }

    state.value = LoadState.Loading(job)
    job.start()
}
