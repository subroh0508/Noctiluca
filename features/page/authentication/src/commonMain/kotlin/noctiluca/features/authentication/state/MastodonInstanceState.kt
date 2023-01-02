package noctiluca.features.authentication.state

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import noctiluca.authentication.domain.usecase.ShowMastodonInstanceUseCase
import noctiluca.components.model.LoadState
import noctiluca.features.authentication.LocalScope
import noctiluca.instance.model.Instance
import org.koin.core.scope.Scope

class MastodonInstanceState(
    private val useCase: ShowMastodonInstanceUseCase,
    private val scope: CoroutineScope,
    private val state: MutableState<LoadState> = mutableStateOf(LoadState.Initial),
) : State<LoadState> by state {
    val loading get() = state.value.loading
    fun getValueOrNull() = state.value.getValueOrNull<Instance>()

    fun fetch(suggest: Instance.Suggest) {
        val job = scope.launch(start = CoroutineStart.LAZY) {
            runCatching { useCase.execute(suggest.domain) }
                .onSuccess { state.value = LoadState.Loaded(it) }
                .onFailure { state.value = LoadState.Error(it) }
        }

        state.value = LoadState.Loading(job)
        job.start()
    }
}

@Composable
fun rememberMastodonInstance(
    saver: Saver<LoadState, Any>,
    scope: Scope = LocalScope.current,
): MastodonInstanceState {
    val coroutineScope = rememberCoroutineScope()
    val loadState = rememberSaveable(stateSaver = saver) { mutableStateOf(LoadState.Initial) }

    return remember { MastodonInstanceState(scope.get(), coroutineScope, loadState) }
}
