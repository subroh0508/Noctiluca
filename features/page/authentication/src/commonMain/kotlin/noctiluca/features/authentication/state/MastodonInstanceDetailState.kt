package noctiluca.features.authentication.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import noctiluca.authentication.domain.usecase.FetchMastodonInstanceUseCase
import noctiluca.features.authentication.LocalScope
import noctiluca.features.components.model.LoadState
import noctiluca.features.components.state.loadLazy
import noctiluca.features.components.state.produceLoadState
import org.koin.core.scope.Scope

@Composable
internal fun rememberMastodonInstanceDetail(
    domain: String,
    scope: Scope = LocalScope.current,
): State<LoadState> {
    val useCase: FetchMastodonInstanceUseCase = remember { scope.get() }

    return produceLoadState(domain) {
        loadLazy { useCase.execute(domain) }
    }
}
