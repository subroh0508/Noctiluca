package noctiluca.features.components.state

import androidx.compose.runtime.MutableState
import kotlinx.coroutines.CoroutineScope
import noctiluca.features.components.model.LoadState
import noctiluca.features.components.utils.loadLazy

interface LoadStateComposeState<T: Any> : MutableState<LoadState> {
    companion object {
        operator fun <T: Any> invoke(
            state: MutableState<LoadState>,
        ) : LoadStateComposeState<T> = Impl(state)
    }

    val loading get() = value is LoadState.Loading
    val loaded get() = value !is LoadState.Initial && value !is LoadState.Loading

    @Suppress("UNCHECKED_CAST")
    fun getValueOrNull(): T? = value.getValueOrNull()
    fun getErrorOrNull() = value.getErrorOrNull()
    fun cancel() = value.cancel()

    fun CoroutineScope.loadLazy(
        block: suspend CoroutineScope.() -> T,
    ) = loadLazy(this@LoadStateComposeState, block)

    private class Impl<T: Any>(
        private val state: MutableState<LoadState>
    ) : LoadStateComposeState<T>, MutableState<LoadState> by state
}