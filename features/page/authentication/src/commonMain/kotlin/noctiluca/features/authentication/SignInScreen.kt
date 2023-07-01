package noctiluca.features.authentication

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.text.intl.Locale
import com.arkivanov.decompose.ComponentContext
import noctiluca.features.authentication.di.SignInFeatureContext
import noctiluca.features.authentication.model.AuthorizeResult
import noctiluca.features.authentication.templates.scaffold.InstanceDetailScaffold
import noctiluca.features.authentication.templates.scaffold.SearchInstanceScaffold
import noctiluca.features.components.FeatureComposable
import noctiluca.features.components.atoms.snackbar.LocalSnackbarHostState
import noctiluca.features.components.di.getKoinRootScope
import org.koin.core.component.KoinScopeComponent

internal val LocalNavigation = compositionLocalOf<SignInNavigation?> { null }
internal val LocalResources = compositionLocalOf { Resources("JA") }
internal val LocalScope = compositionLocalOf { getKoinRootScope() }
internal val LocalAuthorizeResult = compositionLocalOf<AuthorizeResult?> { null }

@Composable
fun SignInScreen(
    domain: String?,
    authorizeResult: AuthorizeResult?,
    rootContext: ComponentContext,
    navigation: SignInNavigation,
) {
    val context = remember { SignInFeatureContext.Factory(rootContext) }

    SignInFeature(
        authorizeResult,
        context,
        navigation,
    ) {
        if (domain == null) {
            SearchInstanceScaffold()
            return@SignInFeature
        }

        InstanceDetailScaffold(domain, context)
    }
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
        LocalNavigation provides navigation,
        LocalSnackbarHostState provides remember { SnackbarHostState() },
    ) { content() }
}
