package noctiluca.features.authentication.state

import androidx.compose.runtime.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import noctiluca.authentication.domain.usecase.RequestAccessTokenUseCase
import noctiluca.authentication.domain.usecase.RequestAppCredentialUseCase
import noctiluca.components.model.LoadState
import noctiluca.components.state.LoadStateComposeState
import noctiluca.components.state.loadLazy
import noctiluca.components.state.produceLoadState
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
) : LoadStateComposeState<Uri> by LoadStateComposeState(state) {
    fun request(instance: Instance) = scope.loadLazy {
        useCase.execute(Hostname(instance.domain), clientName, redirectUri)
    }

    fun clear() { state.value = LoadState.Initial }
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

    return produceLoadState(authorizeCode) {
        val code = authorizeCode?.getCodeOrNull()
        val error = authorizeCode?.getErrorOrNull()
        if (code.isNullOrBlank()) {
            value = error?.let { LoadState.Error(it) } ?: LoadState.Initial
            return@produceLoadState
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
    val authorizedUser = authorizedUserState.getValueOrNull<AuthorizedUser>()

    LaunchedEffect(authorizeCode, authorizeUri) {
        if (authorizeCode?.getCodeOrNull() == null && authorizeUri != null) {
            authorizeUriState.clear()
            navController.openBrowser(authorizeUri)
        }
    }

    LaunchedEffect(authorizedUser) {
        if (authorizedUser == null) {
            return@LaunchedEffect
        }

        navController.navigateToTimeline()
    }
}
