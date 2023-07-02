package noctiluca.features.authentication

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.text.intl.Locale
import com.arkivanov.decompose.Child
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.value.getValue
import noctiluca.features.authentication.model.AuthorizeResult
import noctiluca.features.authentication.templates.scaffold.InstanceDetailScaffold
import noctiluca.features.authentication.templates.scaffold.SearchInstanceScaffold
import noctiluca.features.authentication.viewmodel.MastodonInstanceDetailViewModel
import noctiluca.features.authentication.viewmodel.MastodonInstanceListViewModel
import noctiluca.features.authentication.viewmodel.context.SignInFeatureContext
import noctiluca.features.components.FeatureComposable
import noctiluca.features.components.atoms.snackbar.LocalSnackbarHostState
import noctiluca.features.components.di.getKoinRootScope
import org.koin.core.component.KoinScopeComponent

internal val LocalContext = compositionLocalOf<SignInFeatureContext?> { null }
internal val LocalResources = compositionLocalOf { Resources("JA") }
internal val LocalScope = compositionLocalOf { getKoinRootScope() }
internal val LocalAuthorizeResult = compositionLocalOf<AuthorizeResult?> { null }

@Composable
fun SignInScreen(
    domain: String?,
    authorizeResult: AuthorizeResult?,
    rootContext: ComponentContext,
    navigation: SignInNavigation,
) = SignInFeature(
    authorizeResult,
    SignInFeatureContext.Factory(rootContext),
) { page ->
    when (page) {
        is SignInFeatureContext.Child.MastodonInstanceList -> SearchInstanceScaffold(
            MastodonInstanceListViewModel.Provider(page),
        )

        is SignInFeatureContext.Child.MastodonInstanceDetail -> InstanceDetailScaffold(
            MastodonInstanceDetailViewModel.Provider(
                domain ?: page.domain,
                navigation,
                page,
            )
        )
    }
}

@Composable
private fun SignInFeature(
    authorizeResult: AuthorizeResult?,
    context: SignInFeatureContext,
    content: @Composable (SignInFeatureContext.Child) -> Unit,
) = FeatureComposable(context) {
    CompositionLocalProvider(
        LocalResources provides Resources(Locale.current.language),
        LocalAuthorizeResult provides authorizeResult,
        LocalContext provides context,
        LocalSnackbarHostState provides remember { SnackbarHostState() },
    ) {
        val page by context.childStack.subscribeAsState()

        content(page.active.instance)
    }
}

@Composable
private fun SignInFeature(
    authorizeResult: AuthorizeResult?,
    koinComponent: KoinScopeComponent,
    context: SignInFeatureContext,
    content: @Composable () -> Unit,
) = FeatureComposable(koinComponent) { scope ->
    CompositionLocalProvider(
        LocalResources provides Resources(Locale.current.language),
        LocalScope provides scope,
        LocalAuthorizeResult provides authorizeResult,
        LocalContext provides context,
        LocalSnackbarHostState provides remember { SnackbarHostState() },
    ) { content() }
}
