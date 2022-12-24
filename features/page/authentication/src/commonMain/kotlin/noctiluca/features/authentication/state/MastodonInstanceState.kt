package noctiluca.features.authentication.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import noctiluca.authentication.domain.usecase.SearchMastodonInstancesUseCase
import noctiluca.components.model.LoadState
import noctiluca.features.authentication.CurrentScope
import noctiluca.instance.model.Instance
import org.koin.core.scope.Scope

@Composable
fun rememberMastodonInstances(
    query: String,
    scope: Scope = CurrentScope,
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
