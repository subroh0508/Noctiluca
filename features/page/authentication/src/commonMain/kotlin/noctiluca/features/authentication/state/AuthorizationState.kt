package noctiluca.features.authentication.state

import androidx.compose.runtime.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import noctiluca.authentication.domain.usecase.RequestAccessTokenUseCase
import noctiluca.authentication.domain.usecase.RequestAppCredentialUseCase
import noctiluca.features.authentication.*
import noctiluca.features.authentication.LocalAuthorizeResult
import noctiluca.features.authentication.LocalScope
import noctiluca.features.authentication.model.*
import noctiluca.features.components.model.LoadState
import noctiluca.features.components.state.LoadStateComposeState
import noctiluca.instance.model.Instance
import noctiluca.model.AuthorizedUser
import noctiluca.model.Domain
import noctiluca.model.Uri
import org.koin.core.scope.Scope

internal class AuthorizedUserState(
    private val clientName: String,
    private val redirectUri: Uri,
    private val navigation: SignInNavigation?,
    private val requestAppCredentialUseCase: RequestAppCredentialUseCase,
    private val requestRequestAccessTokenUseCase: RequestAccessTokenUseCase,
    private val state: MutableState<LoadState> = mutableStateOf(LoadState.Initial),
) : LoadStateComposeState<AuthorizedUser> by LoadStateComposeState(state) {
    constructor(
        clientName: String,
        redirectUri: Uri,
        navigation: SignInNavigation?,
        koinScope: Scope,
    ) : this(
        clientName,
        redirectUri,
        navigation,
        koinScope.get(),
        koinScope.get(),
    )

    fun requestAuthorize(
        scope: CoroutineScope,
        instance: Instance,
    ) {
        val domain = Domain(instance.domain)

        val job = scope.launch(start = CoroutineStart.LAZY) {
            runCatching { requestAppCredentialUseCase.execute(domain, clientName, redirectUri) }
                .onSuccess { navigation?.openBrowser(it) }
                .onFailure { value = LoadState.Error(it) }
        }

        value = LoadState.Loading(job)
        job.start()
    }

    fun fetch(
        scope: CoroutineScope,
        result: AuthorizeResult?,
    ) {
        val code = result?.getCodeOrNull()
        val error = result?.getErrorOrNull()

        if (value !is LoadState.Loading) {
            return
        }

        if (code == null) {
            value = error?.let(LoadState::Error) ?: LoadState.Initial
            return
        }

        val job = scope.launch(start = CoroutineStart.LAZY) {
            runCatching { requestRequestAccessTokenUseCase.execute(code, redirectUri) }
                .onSuccess {
                    if (it != null) {
                        navigation?.navigateToTimelines()
                        return@onSuccess
                    }

                    value = LoadState.Error(UnknownException)
                }
                .onFailure { value = LoadState.Error(it) }
        }

        value = LoadState.Loading(job)
        job.start()
    }
}

@Composable
internal fun rememberAuthorizedUser(
    domain: String,
    result: AuthorizeResult? = LocalAuthorizeResult.current,
    navigation: SignInNavigation? = LocalNavigation.current,
    scope: Scope = LocalScope.current,
): AuthorizedUserState {
    val clientName = getString().sign_in_client_name
    val redirectUri = buildRedirectUri(domain)

    val authorizedUser = remember {
        AuthorizedUserState(
            clientName,
            redirectUri,
            navigation,
            scope,
        )
    }

    LaunchedEffect(result) { authorizedUser.fetch(this, result) }

    return authorizedUser
}
