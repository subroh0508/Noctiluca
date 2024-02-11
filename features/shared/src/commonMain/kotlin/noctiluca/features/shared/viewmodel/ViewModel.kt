package noctiluca.features.shared.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import noctiluca.features.shared.model.LoadState
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

expect abstract class ViewModel()

expect val ViewModel.viewModelScope: CoroutineScope

fun ViewModel.launch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit,
) = viewModelScope.launch(
    context,
    start,
    block,
)

fun ViewModel.launchLazy(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend CoroutineScope.() -> Unit,
) = launch(context, CoroutineStart.LAZY, block)

fun ViewModel.launchLazy(
    state: MutableStateFlow<LoadState>,
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend CoroutineScope.() -> Unit,
) {
    val job = launch(context, CoroutineStart.LAZY) {
        runCatching { block() }
            .onSuccess { state.value = LoadState.Loaded }
            .onFailure {
                it.printStackTrace()
                state.value = LoadState.Error(it)
            }
    }

    state.value = LoadState.Loading(job)
    job.start()
}
