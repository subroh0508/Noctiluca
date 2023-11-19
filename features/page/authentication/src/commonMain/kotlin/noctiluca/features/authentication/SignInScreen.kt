package noctiluca.features.authentication

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.text.intl.Locale
import cafe.adriel.voyager.core.registry.screenModule
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import noctiluca.features.authentication.di.SignInComponent
import noctiluca.features.authentication.model.AuthorizeResult
import noctiluca.features.authentication.model.buildAuthorizeResult
import noctiluca.features.authentication.templates.scaffold.InstanceDetailScaffold
import noctiluca.features.authentication.templates.scaffold.SearchInstanceScaffold
import noctiluca.features.authentication.viewmodel.AuthorizeViewModel
import noctiluca.features.authentication.viewmodel.MastodonInstanceDetailViewModel
import noctiluca.features.authentication.viewmodel.MastodonInstanceListViewModel
import noctiluca.features.navigation.MastodonInstanceDetailParams
import noctiluca.features.navigation.MastodonInstanceListParams
import noctiluca.features.navigation.SignIn
import noctiluca.features.navigation.SignInParams
import noctiluca.features.shared.atoms.snackbar.LocalSnackbarHostState

internal val LocalResources = compositionLocalOf { Resources("JA") }

val featureSignInScreenModule = screenModule {
    register<SignIn> { (params) -> SignInScreen(params) }
}

internal data class SignInScreen(
    val params: SignInParams
) : Screen {
    @Composable
    override fun Content() = SignInFeature {

        when (params) {
            is MastodonInstanceListParams -> Navigator(MastodonInstanceListScreen)
            is MastodonInstanceDetailParams -> Navigator(
                MastodonInstanceDetailScreen(
                    params.domain,
                    buildAuthorizeResult(params),
                )
            )
        }
    }
}

internal data object MastodonInstanceListScreen : Screen {
    @Composable
    override fun Content() = SignInFeature {
        val component = remember { SignInComponent() }
        val viewModel = MastodonInstanceListViewModel.Provider(component)

        SearchInstanceScaffold(viewModel)
    }
}

internal data class MastodonInstanceDetailScreen(
    private val domain: String,
    private val authorizeResult: AuthorizeResult? = null,
) : Screen {
    @Composable
    override fun Content() = SignInFeature {
        val component = remember { SignInComponent() }

        val authorizeViewModel = AuthorizeViewModel.Provider(
            domain,
            component,
        )

        HandleAuthorize(authorizeResult, authorizeViewModel)
        HandleDeepLink()

        InstanceDetailScaffold(
            MastodonInstanceDetailViewModel.Provider(
                domain,
                component,
            ),
            authorizeResult,
            authorizeViewModel.isFetchingAccessToken,
        ) { authorizeViewModel.requestAuthorize(it) }
    }
}

@Composable
private fun SignInFeature(
    content: @Composable () -> Unit,
) = CompositionLocalProvider(
    LocalResources provides Resources(Locale.current.language),
    LocalSnackbarHostState provides remember { SnackbarHostState() },
) { content() }
