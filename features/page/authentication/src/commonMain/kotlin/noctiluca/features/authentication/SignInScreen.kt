package noctiluca.features.authentication

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.text.intl.Locale
import noctiluca.features.authentication.model.AuthorizeResult
import noctiluca.features.authentication.model.LocalNavController
import noctiluca.features.authentication.model.NavController
import noctiluca.features.authentication.templates.scaffold.InstanceDetailScaffold
import noctiluca.features.authentication.templates.scaffold.SearchInstanceScaffold
import noctiluca.features.components.FeatureComposable
import noctiluca.features.components.Navigation
import noctiluca.features.components.atoms.snackbar.LocalSnackbarHostState
import noctiluca.features.components.di.getKoinRootScope
import org.koin.core.component.KoinScopeComponent

internal val LocalResources = compositionLocalOf { Resources("JA") }
internal val LocalScope = compositionLocalOf { getKoinRootScope() }
internal val LocalAuthorizeResult = compositionLocalOf<AuthorizeResult?> { null }

@Composable
fun SearchInstanceSuggestsScreen(
    koinComponent: KoinScopeComponent,
    navigation: SignInNavigation,
) = SignInFeature(
    authorizeResult = null,
    koinComponent,
    navigation,
) { SearchInstanceScaffold(navigation) }

@Composable
fun InstanceDetailScreen(
    domain: String?,
    authorizeResult: AuthorizeResult?,
    koinComponent: KoinScopeComponent,
    navigation: SignInNavigation,
) = SignInFeature(
    authorizeResult,
    koinComponent,
    navigation,
) {
    if (domain == null) {
        navigation.backPressed()
        return@SignInFeature
    }

    InstanceDetailScaffold(domain, navigation)
}

@Composable
private fun SignInFeature(
    authorizeResult: AuthorizeResult?,
    koinComponent: KoinScopeComponent,
    navigation: SignInNavigation,
    content: @Composable () -> Unit,
) = FeatureComposable(koinComponent) { scope ->
    CompositionLocalProvider(
        LocalResources provides Resources(Locale.current.language),
        LocalScope provides scope,
        LocalAuthorizeResult provides authorizeResult,
        LocalNavController provides NavController(navigation),
        LocalSnackbarHostState provides remember { SnackbarHostState() },
    ) { content() }
}
