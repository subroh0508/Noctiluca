package noctiluca.features.authentication

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.text.intl.Locale
import cafe.adriel.voyager.core.registry.screenModule
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import noctiluca.features.authentication.model.AuthorizeResult
import noctiluca.features.authentication.model.buildAuthorizeResult
import noctiluca.features.authentication.templates.scaffold.InstanceDetailScaffold
import noctiluca.features.authentication.templates.scaffold.SearchInstanceScaffold
import noctiluca.features.authentication.viewmodel.MastodonInstanceListViewModel
import noctiluca.features.navigation.MastodonInstanceDetailParams
import noctiluca.features.navigation.MastodonInstanceListParams
import noctiluca.features.navigation.SignIn
import noctiluca.features.navigation.SignInParams
import noctiluca.features.shared.atoms.snackbar.LocalSnackbarHostState
import org.koin.core.parameter.parametersOf

internal val LocalResources = compositionLocalOf { Resources("JA") }

val featureSignInScreenModule = screenModule {
    register<SignIn> { (params) -> SignInScreen(params) }
}

internal data class SignInScreen(
    val params: SignInParams,
) : Screen {
    constructor(domain: String) : this(MastodonInstanceDetailParams(domain, null))

    @Composable
    override fun Content() = CompositionLocalProvider(
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
) { viewModel, isFetchingAccessToken ->
    InstanceDetailScaffold(
        getScreenModel { parametersOf(domain) },
        authorizeResult,
        isFetchingAccessToken,
    ) { viewModel.requestAuthorize(it) }
}
