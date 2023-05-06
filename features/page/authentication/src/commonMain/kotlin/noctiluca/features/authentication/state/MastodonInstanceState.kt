package noctiluca.features.authentication.state

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import noctiluca.authentication.domain.usecase.SearchMastodonInstancesUseCase
import noctiluca.features.authentication.LocalScope
import noctiluca.features.authentication.state.saver.SuggestsLoadStateSaver
import noctiluca.features.components.model.LoadState
import org.koin.core.scope.Scope

enum class InstancesTab {
    INFO, EXTENDED_DESCRIPTION, LOCAL_TIMELINE
}

@Composable
internal fun rememberMastodonInstanceSuggests(
    query: String,
    scope: Scope = LocalScope.current,
): State<LoadState> {
    val useCase: SearchMastodonInstancesUseCase = remember { scope.get() }

    val state = rememberSaveable(stateSaver = SuggestsLoadStateSaver) {
        mutableStateOf(query to LoadState.Initial)
    }

    val (prevQuery, loadState) = state.value

    LaunchedEffect(query) {
        if (query == prevQuery) {
            return@LaunchedEffect
        }

        if (query.isBlank()) {
            state.value = query to LoadState.Initial
            return@LaunchedEffect
        }

        val job = launch(start = CoroutineStart.LAZY) {
            runCatching { useCase.execute(query) }
                .onSuccess { state.value = query to LoadState.Loaded(it) }
                .onFailure { state.value = query to LoadState.Error(it) }
        }

        state.value = query to LoadState.Loading(job)
        job.start()
    }

    return derivedStateOf { loadState }
}
