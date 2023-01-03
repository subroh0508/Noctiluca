package noctiluca.components.state

import androidx.compose.runtime.MutableState
import noctiluca.components.model.LoadState

sealed interface LoadStateComposeState : MutableState<LoadState> {
    companion object {
        operator fun invoke(
            state: MutableState<LoadState>,
        ) : LoadStateComposeState = Impl(state)
    }

    val loading get() = value is LoadState.Loading
    val loaded get() = value !is LoadState.Initial && value !is LoadState.Loading

    @Suppress("UNCHECKED_CAST")
    fun <T> getValueOrNull(): T? = value.getValueOrNull()
    fun getErrorOrNull() = value.getErrorOrNull()
    fun cancel() = value.cancel()

    private class Impl(
        private val state: MutableState<LoadState>
    ) : LoadStateComposeState, MutableState<LoadState> by state
}