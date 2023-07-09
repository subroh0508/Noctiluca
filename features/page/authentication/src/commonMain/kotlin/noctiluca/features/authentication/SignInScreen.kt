package noctiluca.features.authentication

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.text.intl.Locale
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import noctiluca.features.authentication.model.AuthorizeResult
import noctiluca.features.authentication.templates.scaffold.InstanceDetailScaffold
import noctiluca.features.authentication.templates.scaffold.SearchInstanceScaffold
import noctiluca.features.authentication.viewmodel.MastodonInstanceDetailViewModel
import noctiluca.features.authentication.viewmodel.MastodonInstanceListViewModel
import noctiluca.features.components.FeatureComposable
import noctiluca.features.components.Navigator
import noctiluca.features.components.atoms.snackbar.LocalSnackbarHostState

internal val LocalNavigator = compositionLocalOf<SignInNavigator.Screen?> { null }
internal val LocalResources = compositionLocalOf { Resources("JA") }
internal val LocalAuthorizeResult = compositionLocalOf<AuthorizeResult?> { null }

@Composable
fun SignInScreen(
    domain: String?,
    authorizeResult: AuthorizeResult?,
    screen: SignInNavigator.Screen,
) = SignInFeature(
    authorizeResult,
    screen,
) { page ->
    when (page) {
        is SignInNavigator.Screen.Child.MastodonInstanceList -> SearchInstanceScaffold(
            MastodonInstanceListViewModel.Provider(screen),
        )

        is SignInNavigator.Screen.Child.MastodonInstanceDetail -> InstanceDetailScaffold(
            MastodonInstanceDetailViewModel.Provider(domain ?: page.domain, screen)
        )
    }
}

@Composable
private fun SignInFeature(
    authorizeResult: AuthorizeResult?,
    screen: SignInNavigator.Screen,
    content: @Composable (SignInNavigator.Screen.Child) -> Unit,
) = FeatureComposable(context = screen) {
    CompositionLocalProvider(
        LocalResources provides Resources(Locale.current.language),
        LocalNavigator provides screen,
        LocalAuthorizeResult provides authorizeResult,
        LocalSnackbarHostState provides remember { SnackbarHostState() },
    ) {
        val page by screen.childStack.subscribeAsState()

        content(page.active.instance)
    }
}
