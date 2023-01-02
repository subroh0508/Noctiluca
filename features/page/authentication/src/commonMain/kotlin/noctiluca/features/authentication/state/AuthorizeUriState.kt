package noctiluca.features.authentication.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import noctiluca.authentication.domain.usecase.RequestAppCredentialUseCase
import noctiluca.components.model.LoadState
import noctiluca.features.authentication.LocalScope
import noctiluca.features.authentication.getString
import noctiluca.model.Hostname
import org.koin.core.scope.Scope

@Composable
fun rememberAuthorizeUri(
    hostname: Hostname?,
    scope: Scope = LocalScope.current,
): State<LoadState> {
    val clientName = getString().sign_in_client_name
    val redirectUri = buildRedirectUri()
    val useCase: RequestAppCredentialUseCase = remember { scope.get() }

    return produceState<LoadState>(
        initialValue = LoadState.Initial,
        hostname,
    ) {
        if (hostname == null) {
            value = LoadState.Initial
            return@produceState
        }

        val job = launch(start = CoroutineStart.LAZY) {
            runCatching { useCase.execute(hostname, clientName, redirectUri) }
                .onSuccess { value = LoadState.Loaded(it) }
                .onFailure {
                    it.printStackTrace()
                    value = LoadState.Error(it)
                }
        }

        value = LoadState.Loading(job)
        job.start()
    }
}
