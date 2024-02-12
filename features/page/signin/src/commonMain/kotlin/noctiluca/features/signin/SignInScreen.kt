package noctiluca.features.signin

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.text.intl.Locale
import cafe.adriel.voyager.core.registry.screenModule
import cafe.adriel.voyager.core.screen.Screen
import noctiluca.features.navigation.MastodonInstanceDetailParams
import noctiluca.features.navigation.MastodonInstanceListParams
import noctiluca.features.navigation.SignIn
import noctiluca.features.navigation.SignInParams
import noctiluca.features.navigation.utils.getFeaturesScreenModel
import noctiluca.features.shared.FeatureComposable
import noctiluca.features.shared.atoms.snackbar.LocalSnackbarHostState
import noctiluca.features.signin.model.AuthorizeResult
import noctiluca.features.signin.model.buildAuthorizeResult
import noctiluca.features.signin.screen.InstanceDetailScaffold
import noctiluca.features.signin.screen.SearchInstanceScaffold

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
        val lazyListState = rememberLazyListState()

        when (params) {
            is MastodonInstanceListParams -> MastodonInstanceListScreen(
                lazyListState,
            )
            is MastodonInstanceDetailParams -> MastodonInstanceDetailScreen(
                params.domain,
                buildAuthorizeResult(params),
            )
        }
    }
}

@Composable
internal fun SignInScreen.MastodonInstanceListScreen(
    lazyListState: LazyListState,
) = SearchInstanceScaffold(getFeaturesScreenModel(), lazyListState)

@Composable
internal fun SignInScreen.MastodonInstanceDetailScreen(
    domain: String,
    authorizeResult: AuthorizeResult?,
) = HandleAuthorize(
    domain,
    authorizeResult,
) { viewModel, isSignInProgress ->
    InstanceDetailScaffold(
        getFeaturesScreenModel(),
        domain,
        isSignInProgress,
    ) { viewModel.requestAuthorize(it) }
}
