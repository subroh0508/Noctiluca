package noctiluca.features.authentication.state

import androidx.compose.runtime.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import noctiluca.authentication.domain.usecase.RequestAccessTokenUseCase
import noctiluca.authentication.domain.usecase.RequestAppCredentialUseCase
import noctiluca.components.model.LoadState
import noctiluca.features.authentication.LocalAuthorizeCode
import noctiluca.features.authentication.LocalScope
import noctiluca.features.authentication.getString
import noctiluca.features.authentication.model.AuthorizeCode
import noctiluca.features.authentication.model.LocalNavController
import noctiluca.features.authentication.model.UnknownException
import noctiluca.features.authentication.model.buildRedirectUri
import noctiluca.instance.model.Instance
import noctiluca.model.AuthorizedUser
import noctiluca.model.Hostname
import noctiluca.model.Uri
import org.koin.core.scope.Scope

internal class AuthorizeUriState(
    private val clientName: String,
    private val redirectUri: Uri,
    private val useCase: RequestAppCredentialUseCase,
    private val scope: CoroutineScope,
    private val state: MutableState<LoadState> = mutableStateOf(LoadState.Initial),
) : State<LoadState> by state {
    val loading get() = state.value.loading
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
internal fun rememberAuthorization(
    scope: Scope = LocalScope.current,
): Triple<AuthorizeUriState, AuthorizeCode?, LoadState> {
    val coroutineScope = rememberCoroutineScope()
    val clientName = getString().sign_in_client_name
    val redirectUri = buildRedirectUri()

    val authorizeCode = LocalAuthorizeCode.current
    val authorizeUriState = remember { AuthorizeUriState(clientName, redirectUri, scope.get(), coroutineScope) }

    val authorizedUserState = produceAuthorizedUserState(authorizeCode, redirectUri, scope).value

    navigateTo(authorizeUriState, authorizeCode, authorizedUserState)

    return Triple(
        authorizeUriState,
        authorizeCode,
        authorizedUserState,
    )
}

@Composable
private fun produceAuthorizedUserState(
    authorizeCode: AuthorizeCode?,
    redirectUri: Uri,
    scope: Scope,
): State<LoadState> {
    val useCase: RequestAccessTokenUseCase = remember { scope.get() }

    return produceState<LoadState>(
        initialValue = LoadState.Initial,
        authorizeCode,
    ) {
        val code = authorizeCode?.getCodeOrNull()
        val error = authorizeCode?.getErrorOrNull()
        if (code.isNullOrBlank()) {
            value = error?.let { LoadState.Error(it) } ?: LoadState.Initial
            return@produceState
        }

        val job = launch(start = CoroutineStart.LAZY) {
            runCatching { useCase.execute(code, redirectUri) }
                .onSuccess { user ->
                    value = user?.let { LoadState.Loaded(it) } ?: LoadState.Error(UnknownException)
                }
                .onFailure { value = LoadState.Error(it) }
        }

        value = LoadState.Loading(job)
        job.start()
    }
}

@Composable
private fun navigateTo(
    authorizeUriState: AuthorizeUriState,
    authorizeCode: AuthorizeCode?,
    authorizedUserState: LoadState,
) {
    val navController = LocalNavController.current

    val authorizeUri = authorizeUriState.getValueOrNull()
    if (authorizeCode == null && authorizeUri != null) {
        navController.openBrowser(authorizeUri)
        return
    }

    if (authorizedUserState.getValueOrNull<AuthorizedUser>() == null) {
        return
    }

    navController.navigateToTimeline()
}
