package noctiluca.features.authentication.state

import androidx.compose.runtime.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import noctiluca.authentication.domain.usecase.RequestAppCredentialUseCase
import noctiluca.components.model.LoadState
import noctiluca.features.authentication.LocalScope
import noctiluca.features.authentication.getString
import noctiluca.features.authentication.model.buildRedirectUri
import noctiluca.instance.model.Instance
import noctiluca.model.Hostname
import noctiluca.model.Uri
import org.koin.core.scope.Scope

class AuthorizeUriState(
    private val clientName: String,
    private val redirectUri: Uri,
    private val useCase: RequestAppCredentialUseCase,
    private val scope: CoroutineScope,
    private val state: MutableState<LoadState> = mutableStateOf(LoadState.Initial),
) : State<LoadState> by state {
    fun getValueOrNull() = state.value.getValueOrNull<Uri>()

    fun request(instance: Instance) {
        val job = scope.launch(start = CoroutineStart.LAZY) {
            runCatching { useCase.execute(Hostname(instance.domain), clientName, redirectUri) }
                .onSuccess { state.value = LoadState.Loaded(it) }
                .onFailure { state.value = LoadState.Error(it) }
        }

        state.value = LoadState.Loading(job)
        job.start()
    }
}

@Composable
fun rememberAuthorizeUri(
    scope: Scope = LocalScope.current,
): AuthorizeUriState {
    val coroutineScope = rememberCoroutineScope()
    val clientName = getString().sign_in_client_name
    val redirectUri = buildRedirectUri()

    return remember { AuthorizeUriState(clientName, redirectUri, scope.get(), coroutineScope) }
}
