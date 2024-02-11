package noctiluca.features.authentication

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.text.intl.Locale
import cafe.adriel.voyager.core.registry.screenModule
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import noctiluca.features.authentication.model.AuthorizeResult
import noctiluca.features.authentication.model.buildAuthorizeResult
import noctiluca.features.authentication.screen.InstanceDetailScaffold
import noctiluca.features.authentication.screen.SearchInstanceScaffold
import noctiluca.features.authentication.viewmodel.MastodonInstanceListViewModel
import noctiluca.features.navigation.MastodonInstanceDetailParams
import noctiluca.features.navigation.MastodonInstanceListParams
import noctiluca.features.navigation.SignIn
import noctiluca.features.navigation.SignInParams
import noctiluca.features.shared.FeatureComposable
import noctiluca.features.shared.atoms.snackbar.LocalSnackbarHostState

internal val LocalResources = compositionLocalOf { Resources("JA") }

val featureSignInScreenModule = screenModule {
    register<SignIn> { (params) -> SignInScreen(params) }
}

internal data class SignInScreen(
    val params: SignInParams,
) : Screen {
    constructor(domain: String) : this(MastodonInstanceDetailParams(domain, null))

    @Composable
    override fun Content() = FeatureComposable(
        LocalResources provides Resources(Locale.current.language),
        LocalSnackbarHostState provides remember { SnackbarHostState() },
    ) {
        when (params) {
            is MastodonInstanceListParams -> MastodonInstanceListScreen()
            is MastodonInstanceDetailParams -> MastodonInstanceDetailScreen(
                params.domain,
                buildAuthorizeResult(params),
            )
        }
    }
}

@Composable
internal fun Screen.MastodonInstanceListScreen() {
    val viewModel: MastodonInstanceListViewModel = getScreenModel()

    SearchInstanceScaffold(viewModel)
}

@Composable
internal fun Screen.MastodonInstanceDetailScreen(
    domain: String,
    authorizeResult: AuthorizeResult?,
) = HandleAuthorize(
    domain,
    authorizeResult,
) { viewModel, isSignInProgress ->
    InstanceDetailScaffold(
        getScreenModel(),
        domain,
        isSignInProgress,
    ) { viewModel.requestAuthorize(it) }
}
