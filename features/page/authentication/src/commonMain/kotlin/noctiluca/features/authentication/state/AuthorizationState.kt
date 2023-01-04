package noctiluca.features.authentication.state

import androidx.compose.runtime.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import noctiluca.authentication.domain.usecase.RequestAccessTokenUseCase
import noctiluca.authentication.domain.usecase.RequestAppCredentialUseCase
import noctiluca.authentication.domain.usecase.ShowMastodonInstanceUseCase
import noctiluca.components.model.LoadState
import noctiluca.components.state.LoadStateComposeState
import noctiluca.components.state.loadLazy
import noctiluca.components.state.produceLoadState
import noctiluca.features.authentication.LocalAuthorizeResult
import noctiluca.features.authentication.LocalScope
import noctiluca.features.authentication.getString
import noctiluca.features.authentication.model.*
import noctiluca.instance.model.Instance
import noctiluca.model.AuthorizedUser
import noctiluca.model.Hostname
import noctiluca.model.Uri
import org.koin.core.scope.Scope

internal class AuthorizedUserState(
    private val instanceLoadState: LoadState,
    private val clientName: String,
    private val redirectUri: Uri,
    private val navController: NavController,
    private val requestAppCredentialUseCase: RequestAppCredentialUseCase,
    private val requestRequestAccessTokenUseCase :RequestAccessTokenUseCase,
    private val scope: CoroutineScope,
    private val state: MutableState<LoadState> = mutableStateOf(LoadState.Initial),
) : LoadStateComposeState<AuthorizedUser> by LoadStateComposeState(state) {
    constructor(
        instanceLoadState: LoadState,
        clientName: String,
        redirectUri: Uri,
        navController: NavController,
        koinScope: Scope,
        coroutineScope: CoroutineScope,
    ) : this(
        instanceLoadState,
        clientName,
        redirectUri,
        navController,
        koinScope.get(),
        koinScope.get(),
        coroutineScope,
        mutableStateOf(if (instanceLoadState.loading) instanceLoadState else LoadState.Initial),
    )

    override val loading get() = instanceLoadState.loading || value.loading
    override fun getErrorOrNull() = instanceLoadState.getErrorOrNull() ?: value.getErrorOrNull()

    fun requestAuthorize() {
        val instance: Instance = instanceLoadState.getValueOrNull() ?: return
        val hostname = Hostname(instance.domain)

        val job = scope.launch(start = CoroutineStart.LAZY) {
            runCatching { requestAppCredentialUseCase.execute(hostname, clientName, redirectUri) }
                .onSuccess { navController.openBrowser(it) }
                .onFailure { value = LoadState.Error(it) }
        }

        value = LoadState.Loading(job)
        job.start()
    }

    fun fetch(result: AuthorizeResult?) {
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
                        navController.navigateToTimeline()
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
internal fun rememberInstanceAndAuthorization(
    query: QueryText,
    result: AuthorizeResult? = LocalAuthorizeResult.current,
    navController: NavController = LocalNavController.current,
    scope: Scope = LocalScope.current,
): Pair<Instance?, AuthorizedUserState> {
    val coroutineScope = rememberCoroutineScope()
    val clientName = getString().sign_in_client_name
    val redirectUri = buildRedirectUri()

    val instance by rememberMastodonInstance(query)

    val authorizedUser = remember(instance) {
        AuthorizedUserState(
            instance,
            clientName,
            redirectUri,
            navController,
            scope,
            coroutineScope,
        )
    }

    LaunchedEffect(result) { authorizedUser.fetch(result) }

    return instance.getValueOrNull<Instance>() to authorizedUser
}

@Composable
private fun rememberMastodonInstance(
    query: QueryText,
    scope: Scope = LocalScope.current,
): State<LoadState> {
    val useCase: ShowMastodonInstanceUseCase = remember { scope.get() }

    return produceLoadState(query.text) {
        if (query !is QueryText.Static) {
            value = LoadState.Initial
            return@produceLoadState
        }

        loadLazy { useCase.execute(query.text)  }
    }
}
