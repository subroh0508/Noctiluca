package noctiluca.features.shared

import androidx.compose.runtime.Composable
import kotlinx.coroutines.Job
import noctiluca.model.LoadState

@Suppress("UNCHECKED_CAST")
@Composable
fun <T : Any> LoadStateComposable(
    loadState: LoadState,
    loading: @Composable (Job) -> Unit,
    fallback: @Composable (Throwable?) -> Unit,
    content: @Composable (T) -> Unit,
) = when (loadState) {
    LoadState.Initial -> fallback(null)
    is LoadState.Loading -> loading(loadState.job)
    is LoadState.Loaded<*> -> content(loadState.value as T)
    is LoadState.Error -> fallback(loadState.cause)
}
