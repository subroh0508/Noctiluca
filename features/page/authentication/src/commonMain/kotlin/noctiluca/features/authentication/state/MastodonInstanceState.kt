package noctiluca.features.authentication.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import noctiluca.authentication.domain.usecase.SearchMastodonInstancesUseCase
import noctiluca.features.authentication.CurrentScope
import noctiluca.instance.model.Instance
import org.koin.core.scope.Scope

@Composable
fun rememberMastodonInstances(
    query: String,
    scope: Scope = CurrentScope,
): State<List<Instance>> {
    val useCase: SearchMastodonInstancesUseCase = remember { scope.get() }

    return produceState(
        listOf(),
        query,
    ) {
        runCatching { useCase.execute(query) }
            .onSuccess { value = it }
            .onFailure { value = listOf() }
    }
}
