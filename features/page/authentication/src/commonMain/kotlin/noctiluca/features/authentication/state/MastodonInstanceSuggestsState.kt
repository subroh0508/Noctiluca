package noctiluca.features.authentication.state

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import noctiluca.authentication.domain.usecase.SearchMastodonInstancesUseCase
import noctiluca.authentication.domain.usecase.ShowMastodonInstanceUseCase
import noctiluca.components.model.LoadState
import noctiluca.features.authentication.LocalScope
import org.koin.core.scope.Scope

@Composable
fun rememberMastodonInstanceSuggests(
    query: String,
    scope: Scope = LocalScope.current,
): State<LoadState> {
    val useCase: SearchMastodonInstancesUseCase = remember { scope.get() }

    return produceState<LoadState>(
        LoadState.Initial,
        query,
    ) {
        if (query.isBlank()) {
            value = LoadState.Initial
            return@produceState
        }

        val job = launch(start = CoroutineStart.LAZY) {
            runCatching { useCase.execute(query) }
                .onSuccess { value = LoadState.Loaded(it) }
                .onFailure { value = LoadState.Error(it) }
        }

        value = LoadState.Loading(job)
        job.start()
    }
}
